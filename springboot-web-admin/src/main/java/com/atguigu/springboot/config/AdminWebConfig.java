package com.atguigu.springboot.config;

import com.atguigu.springboot.interceptor.LoginInterceptor;
import com.atguigu.springboot.interceptor.RedisUrlCountInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AdminWebConfig implements WebMvcConfigurer {

  // Filter, Interceptor 几乎拥有相同的功能 用哪个好?
  // Filter 是 Servlet 定义的原生组件。好处: 脱离Spring应有也能使用
  // Interceptor 是 Spring 定义的接口。 可以使用Spring的自动装配等功能
  private RedisUrlCountInterceptor redisUrlCountInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LoginInterceptor())
        .addPathPatterns("/**") // 所有请求都被拦截，包括静态资源
        .excludePathPatterns("/", "/login", "/css/**", "/fonts/**", "/images/**", "/js/**",
            "/error"); // 放行的请求

//    registry.addInterceptor(redisUrlCountInterceptor)
//        .addPathPatterns("/**")
//        .excludePathPatterns("/", "/login", "/css/**", "/fonts/**", "/images/**", "/js/**",
//            "/error"); // 放行的请求
  }
}
