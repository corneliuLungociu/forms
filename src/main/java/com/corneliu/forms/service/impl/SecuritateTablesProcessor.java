package com.corneliu.forms.service.impl;

import com.corneliu.forms.config.CaseInsensitiveStrLookup;
import com.corneliu.forms.service.TablesProcessor;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

public class SecuritateTablesProcessor implements TablesProcessor {

    private enum TableType {
        CONSUM_ALARMA,
        CONSUM_VIDEO,
        OTHER
    }

    @Override
    public String process(String rawText) {
        Document document = Jsoup.parse(rawText);
        Elements tables = document.body().getElementsByTag("table");
        return processDocumentSecuritate(document, tables);
    }

    private String processDocumentSecuritate(Document document, Elements tables) {
        double consumAlarmaTotal = 0;
        for (Element table : tables) {
            switch (getTableType(table)) {
                case CONSUM_ALARMA: {
                    Map<Integer, ConsumComponenta> sumPerRow = fillSumPerRowForAlarma(table, 3);

                    double totalStandBy = sumPerRow.values().stream().mapToDouble(consum -> consum.standBy).sum() * 23.5;
                    double totalActivat = sumPerRow.values().stream().mapToDouble(consum -> consum.activat).sum() * 0.5;

                    Elements rows = table.select("tr");
                    rows.get(rows.size() - 3).select("td").get(4).text(String.format("%.2f", totalStandBy));
                    rows.get(rows.size() - 2).select("td").last().text(String.format("%.2f", totalActivat));
                    rows.get(rows.size() - 1).select("td").last().text(String.format("%.2f", totalStandBy + totalActivat));

                    consumAlarmaTotal = 1.25 * (totalStandBy + totalActivat) / 1000;

                    break;
                }
                case CONSUM_VIDEO: {
                    Map<Integer, Float> sumPerRow = fillSumPerRowForVideo(table, 3);

                    double total = sumPerRow.values().stream().mapToDouble(Double::valueOf).sum();

                    Elements rows = table.select("tr");
                    rows.get(rows.size() - 3).select("td").last().text(String.format("%.2f", total));
                    rows.get(rows.size() - 2).select("td").last().text(String.format("%.2f", total));
                    rows.get(rows.size() - 1).select("td").last().text(String.format("%.2f", (total / 0.55 / 3) * 10));
                    break;
                }
            }
        }

        String processedDocument = document.html();

        StrSubstitutor dictionarySubst = new StrSubstitutor(
                new CaseInsensitiveStrLookup<>(ImmutableMap
                        .<String, String>builder()
                        .put(TableType.CONSUM_ALARMA.name(), "")
                        .put(TableType.CONSUM_VIDEO.name(), "")
                        .put("CONSUM_ALARMA_TOTAL", String.format("%.2f", consumAlarmaTotal))
                        .build()),
                "[", "]", '!');

        return dictionarySubst.replace(processedDocument);
    }

    private Map<Integer, ConsumComponenta> fillSumPerRowForAlarma(Element table, int footerSize) {

        Map<Integer, ConsumComponenta> tableData = extractDataFrom8ColumnsTable(table, footerSize);
        if (tableData == null) {
            return null;
        }

        Elements rows = table.select("tr");
        for (Map.Entry<Integer, ConsumComponenta> tableDataEntry : tableData.entrySet()) {
            rows.get(tableDataEntry.getKey()).select("td").get(3).text(String.format("%.2f", tableDataEntry.getValue().standBy));
            rows.get(tableDataEntry.getKey()).select("td").get(4).text(String.format("%.2f", tableDataEntry.getValue().standBy * 23.5));
            rows.get(tableDataEntry.getKey()).select("td").get(6).text(String.format("%.2f", tableDataEntry.getValue().activat));
            rows.get(tableDataEntry.getKey()).select("td").get(7).text(String.format("%.2f", tableDataEntry.getValue().activat * 0.5));
        }

        return tableData;
    }

    private Map<Integer, Float> fillSumPerRowForVideo(Element table, int footerSize) {

        Map<Integer, Float> tableData = extractDataFrom4ColumnsTable(table, footerSize);
        if (tableData == null) {
            return null;
        }

        Elements rows = table.select("tr");
        for (Map.Entry<Integer, Float> tableDataEntry : tableData.entrySet()) {
            rows.get(tableDataEntry.getKey()).select("td").last().text(String.format("%.2f", tableDataEntry.getValue()));
        }

        return tableData;
    }

    private Map<Integer, Float> extractDataFrom4ColumnsTable(Element table, int footerSize) {
        Map<Integer, Float> data = new HashMap<>();

        Elements rows = table.select("tr");
        if (rows.size() < footerSize + 1) {
            return null;
        }

        for (int i = 1; i < rows.size() - footerSize; i++) {
            Element row = rows.get(i);
            Elements columns = row.select("td");
            if (columns.size() != 4 ) {
                return null;
            }

            try {
                String col2Text = columns.get(1).text().trim();
                String col3Text = columns.get(2).text().trim();
                if (StringUtils.isEmpty(col2Text) || StringUtils.isEmpty(col3Text)) {
                    continue;
                }

                float col2 = textToNumber(col2Text);
                float col3 = textToNumber(col3Text);
                data.put(i, col2 * col3);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return data;
    }

    private Map<Integer, ConsumComponenta> extractDataFrom8ColumnsTable(Element table, int footerSize) {
        Map<Integer, ConsumComponenta> data = new HashMap<>();

        Elements rows = table.select("tr");
        if (rows.size() < footerSize + 1) {
            return null;
        }

        for (int i = 1; i < rows.size() - footerSize; i++) {
            Element row = rows.get(i);
            Elements columns = row.select("td");
            if (columns.size() != 8 ) {
                return null;
            }

            try {
                String col2Text = columns.get(1).text().trim();
                String col3Text = columns.get(2).text().trim();
                String col6Text = columns.get(5).text().trim();
                if (StringUtils.isEmpty(col2Text) || StringUtils.isEmpty(col3Text) || StringUtils.isEmpty(col6Text)) {
                    continue;
                }

                float col2 = textToNumber(col2Text);
                float col3 = textToNumber(col3Text);
                float col6 = textToNumber(col6Text);
                data.put(i, new ConsumComponenta(col2*col3, col2*col6));
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return data;
    }

    private TableType getTableType(Element table) {
        String text = table.select("tr").first().select("td").first().text();
        for (TableType tableType : TableType.values()) {
            if (text.contains("[" + tableType + "]")) {
                return tableType;
            }
        }

        return TableType.OTHER;
    }

    private float textToNumber(String col1Text) {
        return Float.parseFloat(col1Text.replace(",", "."));
    }

    private class ConsumComponenta {
        private double standBy;
        private double activat;

        public ConsumComponenta(double standBy, double activat) {
            this.standBy = standBy;
            this.activat = activat;
        }

    }
}
