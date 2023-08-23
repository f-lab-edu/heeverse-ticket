package com.heeverse.common;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Delimiter {
    DASH("-"),
    NOT_USE(null)
    ;

    private final String value;

    public String getValue() {
        return value;
    }
}
