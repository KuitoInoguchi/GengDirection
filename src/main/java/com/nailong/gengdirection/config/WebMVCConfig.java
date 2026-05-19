package com.nailong.gengdirection.config;

import com.nailong.gengdirection.behavior.interceptor.BehaviorInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMVCConfig implements WebMvcConfigurer {

    private final BehaviorInterceptor behaviorInterceptor;

    /** 注册行为埋点拦截器，只拦截 /posts 相关路径，避免对全站请求做无谓采集 */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(behaviorInterceptor)
                .addPathPatterns("/posts", "/posts/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
        registry.addMapping("/**")// 允许跨域的路径
                .allowedOriginPatterns("*") // 允许所有的源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
                .allowedHeaders("*") // 允许所有的请求头
                .allowCredentials(true) // 允许携带 Cookie
                .maxAge(3600); // 预检请求的缓存时间
    }
}
