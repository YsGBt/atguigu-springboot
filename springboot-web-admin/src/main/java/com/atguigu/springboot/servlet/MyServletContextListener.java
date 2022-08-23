package com.atguigu.springboot.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@WebListener
public class MyServletContextListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    log.info("MyServletContextListener 监听到项目初始化完成");
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    log.info("MyServletContextListener 监听到项目销毁");
  }
}
