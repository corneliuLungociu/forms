package com.corneliu.forms.config;

import org.apache.commons.lang3.text.StrLookup;

import java.util.Map;

public class CaseInsensitiveStrLookup<V> extends StrLookup<V> {

    private final Map<String, V> dictionary;

    public CaseInsensitiveStrLookup(final Map<String, V> dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public String lookup(final String key) {
        if (dictionary == null) {
            return null;
        }
        Object obj = dictionary.get(key.toUpperCase());
        if (obj == null) {
            return null;
        }

        return obj.toString();
    }
}
