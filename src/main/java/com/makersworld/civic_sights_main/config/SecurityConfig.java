package com.makersworld.civic_sights_main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            .formLogin(formLogin -> formLogin.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Gateway가 검증하므로, 서비스 내에서는 헤더 존재 여부만 체크하거나 모두 허용
                .requestMatchers("/api/articles/premium/{id:[0-9]+}").authenticated() // 프리미엄 뉴스 상세 콘텐츠만 인증 필수
                .requestMatchers("/api/articles/premium").permitAll() // 프리미엄 뉴스 리스트는 인증 없이 허용
                .anyRequest().permitAll() // 그 외는 모두 허용
            );
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Gateway로부터의 요청만 허용
        configuration.setAllowedOrigins(List.of("http://localhost:8000"));
        // 개발 편의상 FE 주소도 추가하는 경우 아래 주석 해제
        // configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}