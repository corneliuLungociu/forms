package com.corneliu.forms.service.impl;

import com.corneliu.forms.config.CaseInsensitiveStrLookup;
import com.corneliu.forms.service.DocumentType;
import com.corneliu.forms.service.DocumentProcessor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class DocumentProcessorImpl implements DocumentProcessor {

    private static final Gson jsonParser = new GsonBuilder().setPrettyPrinting().create();
    private static final String DICTIONARY_FILE = "config/dictionary.json";

    @Override
    public String process(String rawText, Map<String, String> actualDictionary, DocumentType documentType) {
        rawText = cleanMarkup(rawText);

        StrSubstitutor dictionarySubst = new StrSubstitutor(new CaseInsensitiveStrLookup<>(actualDictionary), "[", "]", '!');
        rawText = dictionarySubst.replace(rawText);

        return documentType.tablesProcessor().process(rawText);
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


}
