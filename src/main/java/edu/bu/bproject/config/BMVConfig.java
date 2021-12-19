package edu.bu.bproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class BMVConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/**").excludePathPatterns("/", "/login", "/error/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/index.html").setViewName("index");
//        registry.addViewController("/index.html").setViewName("/");
//        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/admin.html").setViewName("admin");
        registry.addViewController("/4xx.html").setViewName("4xx");
    }
}
