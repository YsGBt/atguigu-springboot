package com.atguigu.springboot.servlet;

import java.util.Arrays;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRegistrationConfig {

  @Bean
  public ServletRegistrationBean myServlet() {
    MyServlet myServlet = new MyServlet();
    return new ServletRegistrationBean(myServlet, "/my", "/my02");
  }

  @Bean
  public FilterRegistrationBean myFilter() {
    MyFilter myFilter = new MyFilter();
//    return new FilterRegistrationBean(myFilter, myServlet());
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(myFilter);
    filterRegistrationBean.setUrlPatterns(Arrays.asList("/css/*", "/my"));
    return filterRegistrationBean;
  }

  @Bean
  public ServletListenerRegistrationBean myListener() {
    MyServletContextListener myServletContextListener = new MyServletContextListener();
    return new ServletListenerRegistrationBean(myServletContextListener);
  }
}
