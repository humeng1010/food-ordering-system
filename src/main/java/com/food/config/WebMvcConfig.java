package com.food.config;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.food.interceptor.LoginInterceptor;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


import java.util.List;
import java.util.Objects;

@Configuration
@Slf4j
//@EnableSwagger2 //开启Swagger
//@EnableKnife4j //使用knife4j(基于swagger)
public class WebMvcConfig implements WebMvcConfigurer {

//    @Bean
//    public Docket createRestApi(){
////        文档类型
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.food.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo apiInfo(){
//        return new ApiInfoBuilder()
//                .title("外卖系统")
//                .version("1.0")
//                .description("接口文档")
//                .build();
//    }
    /**
     * 设置静态资源映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        添加swagger文档资源映射
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resource/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resource/webjars/");
//        后端页面静态资源映射
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
//        前端页面静态资源映射
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    @Autowired
    private LoginInterceptor loginInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/doc.html",
                        "/webjars/**",
                        "/swagger-resource",
                        "/v2/api-docs",
                        "/front/**",
                        "/backend/**",
                        "/error",
                        "/employee/login",
                        "/employee/logout",
                        "/favicon.ico",
                        "/common/**",
                        "/user/sendMsg",
                        "/user/login");
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
