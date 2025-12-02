package com.example.HU4.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtTokenDisplayInterceptor jwtTokenDisplayInterceptor;

    public WebMvcConfig(JwtTokenDisplayInterceptor jwtTokenDisplayInterceptor) {
        this.jwtTokenDisplayInterceptor = jwtTokenDisplayInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenDisplayInterceptor)
                .addPathPatterns("/api/auth/**");
    }
}
