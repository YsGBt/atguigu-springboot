package com.atguigu.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 主程序类
 * @SpringBootApplication: 这是一个SpringBoot应用
 *
 * @SpringBootApplication
 * 等同于
 * @SpringBootConfiguration
 * @EnableAutoConfiguration
 * @ComponentScan("com.atguigu.springboot")
 */
@SpringBootApplication
public class MainApplication {

  public static void main(String[] args) {
    SpringApplication.run(MainApplication.class, args);
  }

}
