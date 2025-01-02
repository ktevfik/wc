package com.asparagus.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum SupportedLocale {
    CHINESE("zh"),
    JAPANESE("ja"),
    KOREAN("ko"),
    THAI("th"),
    TAMIL("ta");

    private final String code;
    private static final Set<String> codes;

    static {
        codes = Arrays.stream(values())
            .map(SupportedLocale::getCode)
            .collect(Collectors.toSet());
    }

    SupportedLocale(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static boolean isMultibyteLocale(String language) {
        return codes.contains(language);
    }
} 