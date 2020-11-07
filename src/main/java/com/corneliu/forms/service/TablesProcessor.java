package com.corneliu.forms.service;

public interface TablesProcessor {

    TablesProcessor DUMMY_TABLE_PROCESSOR = rawText -> rawText;

    String process(String rawText);

}
