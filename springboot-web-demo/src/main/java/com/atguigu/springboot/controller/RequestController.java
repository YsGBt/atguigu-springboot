package com.atguigu.springboot.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RequestController {

  @GetMapping("/goto")
  public String goToPage(HttpServletRequest request) {
    request.setAttribute("msg", "成功");
    request.setAttribute("code", 200);
    return "forward:/success"; // 转发到 /success 请求
  }

  @ResponseBody
  @GetMapping("/success")
  public Map success(@RequestAttribute(value = "msg", required = false) String msg,
      @RequestAttribute(value = "code", required = false) Integer code,
      HttpServletRequest request) {
    Map<String, Object> map = new HashMap<>();
    map.put("annotation_msg", map);
    map.put("reqMethod_msg", request.getAttribute("msg"));
    map.put("code", code);

    Object hello = request.getAttribute("hello");
    Object world = request.getAttribute("world");
    Object message = request.getAttribute("message");

    map.put("hello", hello);
    map.put("world", world);
    map.put("message", map);
    return map;
  }

  @GetMapping("/params")
  public String testParam(Map<String, Object> map,
      Model model,
      HttpServletRequest request,
      HttpServletResponse response) {
    map.put("hello", "world666");
    model.addAttribute("world", "hello666");
    request.setAttribute("message", "HelloWorld");

    Cookie cookie = new Cookie("c1", "v1");
    cookie.setDomain("localhost");
    response.addCookie(cookie);

    return "forward:/success";
  }
}
