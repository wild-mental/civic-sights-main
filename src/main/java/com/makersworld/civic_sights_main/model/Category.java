package com.makersworld.civic_sights_main.model;

/**
 * Enum representing different categories for news articles
 */
public enum Category {
    BASIC_INCOME("basic_income"),
    CIVIC_ENGAGEMENT("civic_engagement"),
    MEGATRENDS("megatrends");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
} 