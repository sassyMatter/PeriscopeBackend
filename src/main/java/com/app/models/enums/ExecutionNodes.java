package com.app.models.enums;

import java.util.Arrays;

public enum ExecutionNodes {
    REST("rest"),
    FUNCTION("func"),
    INVALID("invalid");

    private final String value;

    ExecutionNodes(String val) {
        this.value = val;
    }
    public String getValue() {
        return value;
    }

    public static final ExecutionNodes getByValue(String val){
        return Arrays.stream(ExecutionNodes.values()).filter(enumRole -> enumRole.value.equals(val)).findFirst().orElse(INVALID);
    }
}
