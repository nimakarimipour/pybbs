package co.yiiu.pybbs.config;

import co.yiiu.pybbs.interceptor.CommonInterceptor;
import co.yiiu.pybbs.interceptor.UserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Resource
    private CommonInterceptor commonInterceptor;
    @Resource
    private UserInterceptor userInterceptor;

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        super.addCorsMappings(registry);
        registry.addMapping("/api/**").allowedHeaders("*").allowedMethods("*").allowedOrigins("*").allowCredentials(false);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        // aaaaaaaaa，aaaaaaaaaaa
        registry.addInterceptor(commonInterceptor).addPathPatterns("/**");
        // aaaaa，aaaaaaaa
        registry.addInterceptor(userInterceptor).addPathPatterns("/settings", "/settings/*", "/topic/create", "/topic/edit/*");
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/", "file:./static/");
    }

    // aaaaaaaa
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return sessionLocaleResolver;
    }

    // aaput deleteaaaaaaa
    //  @Bean
    //  public FormContentFilter formContentFilter() {
    //    return new FormContentFilter();
    //  }

}
