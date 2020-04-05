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

public class IncendiuTablesProcessor implements TablesProcessor {

    private static final String MARKER_TABEL_INCENDIU = "TABEL_INCENDIU";
    private static final String OTHER = "OTHER";

    @Override
    public String process(String rawText) {
        Document document = Jsoup.parse(rawText);
        Elements tables = document.body().getElementsByTag("table");

        for (Element table : tables) {
            String tableType = getTableType(table);
            switch (tableType) {
                case MARKER_TABEL_INCENDIU: processTableIncendiu(table); break;
                case OTHER: break;
                default: throw new UnsupportedOperationException("Unknown table type: " + tableType);
            }
        }

        String processedDocument = document.html();

        StrSubstitutor dictionarySubst = new StrSubstitutor(
                new CaseInsensitiveStrLookup<>(ImmutableMap
                        .<String, String>builder()
                        .put(MARKER_TABEL_INCENDIU, "")
                        .build()),
                "[", "]", '!');

        return dictionarySubst.replace(processedDocument);

    }

    private void processTableIncendiu(Element table) {
        double totalStandBy = 0 ;
        double totalActiv  = 0;

        int footerSize = 2;
        Elements rows = table.select("tr");
        if (rows.size() < footerSize + 1) {
            return;
        }

        for (int i = 1; i < rows.size() - footerSize; i++) {
            Element row = rows.get(i);
            Elements columns = row.select("td");
            if (columns.size() != 6 ) {
                return;
            }

            try {
                String col1Text = columns.get(1).text().trim();
                String col2Text = columns.get(2).text().trim();
                String col4Text = columns.get(4).text().trim();
                if (StringUtils.isEmpty(col1Text) || StringUtils.isEmpty(col2Text) || StringUtils.isEmpty(col4Text)) {
                    continue;
                }

                float bucati = textToNumber(col1Text);
                float consumStandBy = textToNumber(col2Text);
                float consumActiv = textToNumber(col4Text);
                columns.get(3).text(String.format("%.3f", bucati * consumStandBy));
                columns.get(5).text(String.format("%.3f", bucati * consumActiv));

                totalActiv += bucati * consumActiv;
                totalStandBy += bucati * consumStandBy;
            } catch (NumberFormatException e) {
                // do nothing. just continue to the next row.
            }
        }

        Elements coloanePenultimulRand = rows.last().previousElementSibling().select("td");
        Elements coloaneUltimulRand = rows.last().select("td");

        coloanePenultimulRand.get(1).text(String.format("%.3f", totalStandBy * 48));
        coloanePenultimulRand.get(3).text(String.format("%.3f", totalActiv / 2));
        coloaneUltimulRand.last().text(String.format("%.3f", (totalStandBy * 48 + totalActiv / 2) / 1000));
    }

    private String getTableType(Element table) {
        String text = table.select("tr").first().select("td").first().text();
        if (text.contains("[" + MARKER_TABEL_INCENDIU + "]")) {
            return MARKER_TABEL_INCENDIU;
        }

        return OTHER;
    }

    private float textToNumber(String col1Text) {
        return Float.parseFloat(col1Text.replace(",", "."));
    }
}
