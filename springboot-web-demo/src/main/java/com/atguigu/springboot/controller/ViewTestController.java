package com.atguigu.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewTestController {

  @GetMapping("/atguigu")
  public String atguigui(Model model) {
    // model 中的数据会被放到请求域中 request.setAttribute("xxx", xxxx)
    model.addAttribute("msg", "你好 guigu");
    model.addAttribute("link", "https://www.baidu.com");
    return "success";
  }
}
