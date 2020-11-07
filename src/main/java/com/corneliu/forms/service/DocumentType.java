package com.corneliu.forms.service;

import com.corneliu.forms.service.impl.IncendiuTablesProcessor;
import com.corneliu.forms.service.impl.SecuritateTablesProcessor;

public enum DocumentType {
    INCENDIU (new IncendiuTablesProcessor()),
    SECURITATE (new SecuritateTablesProcessor()),
    SIMPLE(TablesProcessor.DUMMY_TABLE_PROCESSOR);

    private final TablesProcessor tablesProcessor;
    private String name;

    DocumentType(TablesProcessor tablesProcessor) {
        this.tablesProcessor = tablesProcessor;
        this.name = this.name();
    }

    public TablesProcessor tablesProcessor() {
        return tablesProcessor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = this.name() + "_" + name;
    }
}
