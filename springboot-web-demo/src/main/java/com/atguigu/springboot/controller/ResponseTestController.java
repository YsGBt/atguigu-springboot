package com.atguigu.springboot.controller;

import com.atguigu.springboot.bean.Person;
import com.atguigu.springboot.bean.Pet;
import java.util.Date;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ResponseTestController {

  @ResponseBody
  @GetMapping("/test/person")
  public Person getPerson() {
    return new Person("张三", 28, new Date(), new Pet());
  }
}
