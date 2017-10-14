package com.corneliu.forms.service.impl;

import com.corneliu.forms.config.CaseInsensitiveStrLookup;
import com.corneliu.forms.service.TextProcessor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
import java.util.Map;

public class TextProcessorImpl implements TextProcessor {

    public static final Gson jsonParser = new GsonBuilder().setPrettyPrinting().create();
    private String configFile = "config/dictionary.json";

    @Override
    public String process(String rawText, Map<String, String> actualDictionary) {
        StrSubstitutor subst = new StrSubstitutor(new CaseInsensitiveStrLookup<>(actualDictionary), "[", "]", '!');

        Document document = Jsoup.parse(rawText);
        document.body().getElementsByTag("table").forEach(this::processTable);

        return subst.replace(document.html());

    }

    private void processTable(Element table) {
        // only process tables with 4 columns
        if (table.select("td").size() % 4 != 0) {
            return;
        }

        float grandTotal = 0;
        Elements rows = table.select("tr");
        for (Element row : rows) {
            grandTotal += processRow(row);
        }

        rows.last().select("td").last().text(String.format("%.2f", grandTotal) + "");
    }

    private float processRow(Element row) {
        Elements columns = row.select("td");
        String quantity = columns.get(1).text();
        String pricePerUnit = columns.get(2).text();

        try {
            float quantityNum = Float.parseFloat(quantity);
            float pricePerUnitNum = Float.parseFloat(pricePerUnit);
            float rowTotal = quantityNum * pricePerUnitNum;
            columns.get(3).text(String.format("%.2f", rowTotal));

            return rowTotal;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Map<String, String> getDictionary() throws FileNotFoundException {
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        return jsonParser.fromJson(new FileReader(configFile), type);
    }

    @Override
    public void saveDictionary(Map<String, String> actualDictionary) throws IOException {
        String dictionaryString = jsonParser.toJson(actualDictionary);
        Files.write(Paths.get(configFile), dictionaryString.getBytes("UTF-8"));
    }
}
