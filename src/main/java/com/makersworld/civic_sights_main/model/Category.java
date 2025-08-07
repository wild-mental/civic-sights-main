package com.makersworld.civic_sights_main.model;

/**
 * Enum representing different categories for news articles
 */
public enum Category {
    BASIC_INCOME("basic-income"),
    CIVIC_ENGAGEMENT("civic-engagement"),
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
    
    /**
     * URL path에서 Category를 찾는 메서드
     * "civic-engagement" -> CIVIC_ENGAGEMENT
     * "basic-income" -> BASIC_INCOME
     */
    public static Category fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Category value cannot be null or empty");
        }
        
        // value로 매핑 시도
        for (Category category : values()) {
            if (category.getValue().equalsIgnoreCase(value.trim())) {
                return category;
            }
        }
        
        // enum 이름으로 매핑 시도 
        try {
            return valueOf(value.toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + value + 
                ". Valid categories are: basic-income, civic-engagement, megatrends");
        }
    }
} 