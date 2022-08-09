package com.atguigu.springboot.controller;

import com.atguigu.springboot.bean.Car;
import com.atguigu.springboot.bean.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

  @Autowired
  private Car car;

  @Autowired
  private Person person;

  @RequestMapping("/hello")
  public String hello() {
    log.info("请求HelloWorld页面");
    return "Hello, Spring Boot!";
  }

  @RequestMapping("/car")
  public Car car() {
    return car;
  }

  @RequestMapping("/person")
  public Person person() {
    return person;
  }
}
