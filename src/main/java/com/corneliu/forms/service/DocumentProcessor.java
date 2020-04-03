package com.corneliu.forms.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface DocumentProcessor {

    String process(String rawText, Map<String, String> actualDictionary, DocumentType documentType);

    String cleanMarkup(String rawText);

    Map<String, String> getDictionary() throws FileNotFoundException;

    void saveDictionary(Map<String, String> actualDictionary) throws IOException;
}
