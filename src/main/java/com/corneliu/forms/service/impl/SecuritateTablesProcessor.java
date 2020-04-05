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
        CONSUM_ALARMA_STANDBY,
        CONSUM_ALARMA_ACTIVAT,
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
        double consumAlarmaStandby = 0;
        double consumAlarmaActivat = 0;
        for (Element table : tables) {
            switch (getTableType(table)) {
                case CONSUM_ALARMA_STANDBY: {
                    Map<Integer, Float> sumPerRow = fillSumPerRow(table, 3);

                    double total = sumPerRow.values().stream().mapToDouble(Double::valueOf).sum();

                    Elements rows = table.select("tr");
                    rows.get(rows.size() - 3).select("td").last().text(String.format("%.2f", total));
                    rows.get(rows.size() - 2).select("td").last().text(String.format("%.2f", total * 23.5 / 1000));
                    rows.get(rows.size() - 1).select("td").last().text(String.format("%.2f", total * 23.5 / 1000));
                    consumAlarmaActivat = total * 23.5 / 1000;
                    break;
                }
                case CONSUM_ALARMA_ACTIVAT: {
                    Map<Integer, Float> sumPerRow = fillSumPerRow(table, 3);

                    double total = sumPerRow.values().stream().mapToDouble(Double::valueOf).sum();

                    Elements rows = table.select("tr");
                    rows.get(rows.size() - 3).select("td").last().text(String.format("%.2f", total));
                    rows.get(rows.size() - 2).select("td").last().text(String.format("%.2f", total * 0.5 / 1000));
                    rows.get(rows.size() - 1).select("td").last().text(String.format("%.2f", total * 0.5 / 1000));
                    consumAlarmaStandby = total * 0.5 / 1000;
                    break;
                }
                case CONSUM_VIDEO: {
                    Map<Integer, Float> sumPerRow = fillSumPerRow(table, 3);

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
                        .put(TableType.CONSUM_ALARMA_STANDBY.name(), "")
                        .put(TableType.CONSUM_ALARMA_ACTIVAT.name(), "")
                        .put(TableType.CONSUM_VIDEO.name(), "")
                        .put("CONSUM_ALARMA_TOTAL", String.valueOf((consumAlarmaActivat + consumAlarmaStandby) * 1.25))
                        .build()),
                "[", "]", '!');

        return dictionarySubst.replace(processedDocument);
    }

    private Map<Integer, Float> fillSumPerRow(Element table, int footerSize) {

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

}
