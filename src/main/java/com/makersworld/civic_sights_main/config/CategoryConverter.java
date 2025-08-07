package com.makersworld.civic_sights_main.config;

import com.makersworld.civic_sights_main.model.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Spring Converter to convert String to Category enum
 * Handles URL path variables like "civic-engagement" -> CIVIC_ENGAGEMENT
 */
@Component
public class CategoryConverter implements Converter<String, Category> {
    
    @Override
    public Category convert(@NonNull String source) {
        if (source == null || source.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        
        // 대소문자 구분하지 않고 value로 매핑
        for (Category category : Category.values()) {
            if (category.getValue().equalsIgnoreCase(source.trim())) {
                return category;
            }
        }
        
        // value로 매핑되지 않으면 enum 이름으로 시도
        try {
            return Category.valueOf(source.toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + source + 
                ". Valid categories are: basic-income, civic-engagement, megatrends");
        }
    }
}