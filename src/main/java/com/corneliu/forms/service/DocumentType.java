package com.corneliu.forms.service;

import com.corneliu.forms.service.impl.IncendiuTablesProcessor;
import com.corneliu.forms.service.impl.SecuritateTablesProcessor;

public enum DocumentType {
    INCENDIU (new IncendiuTablesProcessor()),
    SECURITATE (new SecuritateTablesProcessor());

    DocumentType(TablesProcessor tablesProcessor) {
        this.tablesProcessor = tablesProcessor;
    }

    private TablesProcessor tablesProcessor;

    public TablesProcessor tablesProcessor() {
        return tablesProcessor;
    }
}
