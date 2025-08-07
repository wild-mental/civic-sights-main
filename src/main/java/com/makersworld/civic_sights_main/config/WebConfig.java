package com.makersworld.civic_sights_main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC Configuration
 * Registers custom converters for automatic type conversion
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private CategoryConverter categoryConverter;
    
    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        registry.addConverter(categoryConverter);
    }
}