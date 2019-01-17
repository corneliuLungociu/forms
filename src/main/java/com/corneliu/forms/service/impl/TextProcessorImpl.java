package com.corneliu.forms.service.impl;

import com.corneliu.forms.config.CaseInsensitiveStrLookup;
import com.corneliu.forms.service.TextProcessor;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TextProcessorImpl implements TextProcessor {

    private static final Gson jsonParser = new GsonBuilder().setPrettyPrinting().create();
    private static final String DICTIONARY_FILE = "config/dictionary.json";

    private enum TableType {
        CONSUM_ALARMA_STANDBY,
        CONSUM_ALARMA_ACTIVAT,
        CONSUM_VIDEO,
        OTHER
    }

    @Override
    public String process(String rawText, Map<String, String> actualDictionary) {
        rawText = cleanMarkup(rawText);

        StrSubstitutor dictionarySubst = new StrSubstitutor(new CaseInsensitiveStrLookup<>(actualDictionary), "[", "]", '!');
        rawText = dictionarySubst.replace(rawText);

        Document document = Jsoup.parse(rawText);
        Elements tables = document.body().getElementsByTag("table");

        double consumAlarmaStandby = 0;
        double consumAlarmaActivat = 0;
        for (Element table : tables) {
            switch (getTableType(table)) {
                case CONSUM_ALARMA_STANDBY: {
                    Map<Integer, Float> sumPerRow = fillSumPerRow(table, 3);

                    double total = sumPerRow.values().stream().mapToDouble(Double::valueOf).sum();

                    Elements rows = table.select("tr");
                    rows.get(rows.size() - 3).select("td").last().text(String.format("%.2f", total));
                    rows.get(rows.size() - 2).select("td").last().text(String.format("%.2f", total * 1000 * 23.5));
                    rows.get(rows.size() - 1).select("td").last().text(String.format("%.2f", total * 1000 * 23.5));
                    consumAlarmaActivat = total * 1000 * 23.5;
                    break;
                }
                case CONSUM_ALARMA_ACTIVAT: {
                    Map<Integer, Float> sumPerRow = fillSumPerRow(table, 3);

                    double total = sumPerRow.values().stream().mapToDouble(Double::valueOf).sum();

                    Elements rows = table.select("tr");
                    rows.get(rows.size() - 3).select("td").last().text(String.format("%.2f", total));
                    rows.get(rows.size() - 2).select("td").last().text(String.format("%.2f", total * 1000 * 0.5));
                    rows.get(rows.size() - 1).select("td").last().text(String.format("%.2f", total * 1000 * 0.5));
                    consumAlarmaStandby = total * 1000 * 0.5;
                    break;
                }
                case CONSUM_VIDEO: {
                    Map<Integer, Float> sumPerRow = fillSumPerRow(table, 3);

                    double total = sumPerRow.values().stream().mapToDouble(Double::valueOf).sum();

                    Elements rows = table.select("tr");
                    rows.get(rows.size() - 3).select("td").last().text(String.format("%.2f", total));
                    rows.get(rows.size() - 2).select("td").last().text(String.format("%.2f", total));
                    rows.get(rows.size() - 1).select("td").last().text(String.format("%.2f", total / 0.55 / 3));
                    break;
                }
            }
        }

        String processedDocument = document.html();

        dictionarySubst = new StrSubstitutor(
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

    @Override
    public String cleanMarkup(String rawText) {
        return rawText.replaceAll("(?si)<colgroup.*?</colgroup>", "")
                .replaceAll("(?si)<col.*?</col>", "")
                .replaceAll("(?si)<col.*?>", "")
                .replaceAll("(?si)<o.*?</o>", "")
                .replaceAll("(?si)<o:p.*?</o:p>", "");
    }

    @Override
    public Map<String, String> getDictionary() throws FileNotFoundException {
        Type type = new TypeToken<LinkedHashMap<String, String>>() {
        }.getType();
        return jsonParser.fromJson(new FileReader(DICTIONARY_FILE), type);
    }

    @Override
    public void saveDictionary(Map<String, String> actualDictionary) throws IOException {
        String dictionaryString = jsonParser.toJson(actualDictionary);
        Files.write(Paths.get(DICTIONARY_FILE), dictionaryString.getBytes("UTF-8"));
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

                float col2 = Float.parseFloat(col2Text);
                float col3 = Float.parseFloat(col3Text);
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
}
