package com.example.HU4.infrastructure.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class ResponseBodyCaptureConfig {

    @Bean
    public FilterRegistrationBean<ContentCachingFilter> contentCachingFilter() {
        FilterRegistrationBean<ContentCachingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ContentCachingFilter());
        registrationBean.addUrlPatterns("/api/auth/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    private static class ContentCachingFilter implements Filter {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {

            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(
                    (HttpServletRequest) request);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(
                    (HttpServletResponse) response);

            chain.doFilter(requestWrapper, responseWrapper);

            responseWrapper.copyBodyToResponse();
        }
    }
}
