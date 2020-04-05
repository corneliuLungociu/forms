package com.corneliu.forms.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface DocumentProcessor {

    String process(String rawText, Map<String, String> actualDictionary, DocumentType documentType);

    String cleanMarkup(String rawText);

    Map<String, String> getDictionary(DocumentType documentType) throws FileNotFoundException;

    void saveDictionary(DocumentType documentType, Map<String, String> actualDictionary) throws IOException;
}
