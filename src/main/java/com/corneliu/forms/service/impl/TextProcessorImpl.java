package com.corneliu.forms.service.impl;

import com.corneliu.forms.config.CaseInsensitiveStrLookup;
import com.corneliu.forms.service.TextProcessor;
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
import java.util.Map;

public class TextProcessorImpl implements TextProcessor {

    public static final Gson jsonParser = new GsonBuilder().setPrettyPrinting().create();
    private String configFile = "config/dictionary.json";

    @Override
    public String process(String rawText, Map<String, String> actualDictionary) {
        StrSubstitutor subst = new StrSubstitutor(new CaseInsensitiveStrLookup<>(actualDictionary), "[", "]", '!');
        return subst.replace(rawText);

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
