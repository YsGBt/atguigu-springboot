package com.atguigu.springboot.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
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
  public Map success(@RequestAttribute("msg") String msg,
      @RequestAttribute("code") Integer code,
      HttpServletRequest request) {
    Map<String, Object> map = new HashMap<>();
    map.put("annotation_msg", map);
    map.put("reqMethod_msg", request.getAttribute("msg"));
    map.put("code", code);
    return map;
  }
}
