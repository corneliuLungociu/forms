package com.corneliu.forms.service.impl;

import com.corneliu.forms.config.CaseInsensitiveStrLookup;
import com.corneliu.forms.service.TextProcessor;
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
import java.util.Map;

public class TextProcessorImpl implements TextProcessor {

    private static final Gson jsonParser = new GsonBuilder().setPrettyPrinting().create();
    private static final String DICTIONARY_FILE = "config/dictionary.json";
    public static final int NUMBER_OF_FOOTER_ROWS_FOR_4_COL_TABLE = 6;
    public static final int INVERSE_INDEX_TOTAL = 6;
    private static final int INVERSE_INDEX_TOTAL_PER_DAY = 5;
    public static final int INVERSE_INDEX_TOTAL_SIRENE = 4;
    public static final int INVERSE_INDEX_TOTAL_SIRENE_HALF = 3;
    private static final int INVERSE_INDEX_TOTAL_WITH_SIRENE_PER_DAY = 2;
    private static final int INVERSE_INDEX_TOTAL_FINAL = 1;

    @Override
    public String process(String rawText, Map<String, String> actualDictionary) {
        rawText = cleanMarkup(rawText);

        StrSubstitutor dictionarySubst = new StrSubstitutor(new CaseInsensitiveStrLookup<>(actualDictionary), "[", "]", '!');
        rawText = dictionarySubst.replace(rawText);

        Document document = Jsoup.parse(rawText);
        document.body().getElementsByTag("table").forEach(this::processTable);

        return document.html();
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
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        return jsonParser.fromJson(new FileReader(DICTIONARY_FILE), type);
    }

    @Override
    public void saveDictionary(Map<String, String> actualDictionary) throws IOException {
        String dictionaryString = jsonParser.toJson(actualDictionary);
        Files.write(Paths.get(DICTIONARY_FILE), dictionaryString.getBytes("UTF-8"));
    }

    private void processTable(Element table) {

        Elements rows = table.select("tr");
        if (rows.isEmpty()) {
            return;
        }

        int nrOfColumns = rows.get(0).select("td").size();
        switch (nrOfColumns) {
            case 4: process4ColumnsTable(table); break;
            case 5: process5ColsTable(); break;
            default:
        }
    }

    private void    process4ColumnsTable(Element table) {

        Map<Integer, Float> tableData = extractDataFrom4ColumnsTable(table);
        if (tableData == null) {
            return;
        }

        Elements rows = table.select("tr");
        float total = 0;
        for (Map.Entry<Integer, Float> tableDataEntry : tableData.entrySet()) {
            rows.get(tableDataEntry.getKey()).select("td").last().text(String.format("%.2f", tableDataEntry.getValue()));
            total += tableDataEntry.getValue();
        }

        float totalSirene = tableData.get(rows.size() - INVERSE_INDEX_TOTAL_SIRENE);
        float totalSireneHalf = totalSirene / 2;
        total -= totalSirene;
        float totalPerDay = total * 23.5f;
        float totalFinal = (totalSireneHalf + totalPerDay) / 1000;

        rows.get(rows.size() - INVERSE_INDEX_TOTAL).select("td").last().text(String.format("%.2f", total));
        rows.get(rows.size() - INVERSE_INDEX_TOTAL_PER_DAY).select("td").last().text(String.format("%.2f", totalPerDay));
        rows.get(rows.size() - INVERSE_INDEX_TOTAL_SIRENE_HALF).select("td").last().text(String.format("%.2f", totalSireneHalf));
        rows.get(rows.size() - INVERSE_INDEX_TOTAL_WITH_SIRENE_PER_DAY).select("td").last().text(String.format("%.2f", totalFinal));
        rows.get(rows.size() - INVERSE_INDEX_TOTAL_FINAL).select("td").last().text(String.format("%.2f", totalFinal));
    }

    private Map<Integer, Float> extractDataFrom4ColumnsTable(Element table) {
        Map<Integer, Float> data = new HashMap<>();

        Elements rows = table.select("tr");
        if (rows.size() < 7) {
            return null;
        }

        for (int i = 1; i < rows.size() - NUMBER_OF_FOOTER_ROWS_FOR_4_COL_TABLE; i++) {
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

        int rowNrForSirene = rows.size() - INVERSE_INDEX_TOTAL_SIRENE;
        Element rowSirene = rows.get(rowNrForSirene);
        Elements columns = rowSirene.select("td");
        if (columns.size() != 4 ) {
            return null;
        }
        try {
            float col2 = Float.parseFloat(columns.get(1).text().trim());
            float col3 = Float.parseFloat(columns.get(2).text().trim());
            data.put(rowNrForSirene, col2 * col3);
        } catch (NumberFormatException e) {
            return null;
        }

        return data;
    }

    private void process5ColsTable() {

    }
}
