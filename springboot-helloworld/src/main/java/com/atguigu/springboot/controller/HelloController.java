package com.atguigu.springboot.controller;

import com.atguigu.springboot.bean.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @Autowired
  private Car car;

  @RequestMapping("/hello")
  public String hello() {
    return "Hello, Spring Boot!";
  }

  @RequestMapping("/car")
  public Car car() {
    return car;
  }
}
