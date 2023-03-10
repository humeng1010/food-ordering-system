package com.food.config;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.food.interceptor.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 设置静态资源映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    @Autowired
    private LoginInterceptor loginInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/front/**",
                        "/backend/**",
                        "/error",
                        "/employee/login",
                        "/employee/logout",
                        "/favicon.ico",
                        "/common/**");
    }

    /**
     * Long 2 String
     */
    static class LongToStringValueFilter implements ValueFilter {
        @Override
        public Object process(Object object, String name, Object value) {
            if (value instanceof Long && !Objects.equals(name,"total")) {
                return String.valueOf(value);
            }
            return value;
        }
    }

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter(){
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
//        值全部使用String 设置序列化程序
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteNonStringValueAsString);
//        只把Long转为String 设置序列化过滤器
        fastJsonConfig.setSerializeFilters(new LongToStringValueFilter());
        converter.setFastJsonConfig(fastJsonConfig);
        return converter;
    }
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(fastJsonHttpMessageConverter());
    }

}
