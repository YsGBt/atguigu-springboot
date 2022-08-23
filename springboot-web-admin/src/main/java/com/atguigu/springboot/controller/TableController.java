package com.atguigu.springboot.controller;

import com.atguigu.springboot.bean.User;
import com.atguigu.springboot.exception.TooManyUserException;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TableController {

  @GetMapping("/basic_table")
  public String basic_table() {
    return "table/basic_table";
  }

  @GetMapping("/dynamic_table")
  public String dynamic_table(Model model) {
    // 表格内容的遍历
    List<User> users = Arrays.asList(new User("张三", "123456"),
        new User("lisa", "123444"),
        new User("haha", "aaaa"),
        new User("nana", "bbbb"));

    model.addAttribute("users", users);

    return "table/dynamic_table";
  }

  @GetMapping("/responsive_table")
  public String responsive_table() {
    return "table/responsive_table";
  }

  @GetMapping("/editable_table")
  public String editable_table() {
    return "table/editable_table";
  }

  @GetMapping("/table_error_example")
  public String table_error() {
    throw new TooManyUserException();
  }

}
