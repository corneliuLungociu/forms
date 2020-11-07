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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DocumentProcessorImpl implements DocumentProcessor {

    private static final Gson jsonParser = new GsonBuilder().setPrettyPrinting().create();
    private static final String DOC_TYPE_PLACEHOLDER = "[TYPE]";
    private static final String CONFIG_DICTIONARY_PREFIX = "config/dictionary_";
    private static final String DICTIONARY_FILE = CONFIG_DICTIONARY_PREFIX + DOC_TYPE_PLACEHOLDER + ".json";

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
    public Map<String, String> getDictionary(DocumentType documentType) throws FileNotFoundException {
        Type type = new TypeToken<LinkedHashMap<String, String>>() {}.getType();
        return jsonParser.fromJson(new FileReader(DICTIONARY_FILE.replace(DOC_TYPE_PLACEHOLDER, documentType.getName())), type);
    }

    @Override
    public void createDictionary(DocumentType documentType) throws IOException {
        String newDictionaryFileName = DICTIONARY_FILE.replace(DOC_TYPE_PLACEHOLDER, documentType.getName());
        Paths.get(newDictionaryFileName).toFile().createNewFile();

        saveDictionary(documentType, new HashMap<>());
    }

    @Override
    public List<String> getAllCustomDictionaries() {
        try {
            return Files.list(Paths.get("config"))
                    .filter(path -> path.toFile().getName().contains(DocumentType.SIMPLE.name()))
                    .map(path -> path.getFileName().toString()
                            .replace("dictionary_SIMPLE_", "")
                            .replace(".json", "")
                            .replace("_", " ")
                    )
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveDictionary(DocumentType documentType, Map<String, String> actualDictionary) throws IOException {
        String dictionaryString = jsonParser.toJson(actualDictionary);
        Files.write(Paths.get(DICTIONARY_FILE.replace(DOC_TYPE_PLACEHOLDER, documentType.getName())), dictionaryString.getBytes("UTF-8"));
    }


}
