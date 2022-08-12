package com.atguigu.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParameterTestController {

  @RequestMapping("/car/{id}/owner/{username}")
  public Map<String, Object> getCarByPathVariable(@PathVariable("id") Integer carId,
      @PathVariable("username") String name,
      @PathVariable Map<String, String> pv,
      @RequestHeader("User-Agent") String userAgent,
      @RequestHeader Map<String, String> header,
      @RequestParam("age") Integer age,
      @RequestParam("inters") List<String> interest,
      @RequestParam Map<String, String> params,
      @CookieValue("_ga") String _ga,
      @CookieValue("_ga") Cookie cookie) {
    Map<String, Object> map = new HashMap<>();
    map.put("id", carId);
    map.put("name", name);
    map.put("pv", pv);
    map.put("User-Agent", userAgent);
    map.put("header", header);
    map.put("age", age);
    map.put("interest", interest);
    map.put("params", params);
    map.put("_ga", _ga);
    System.out.println(cookie);

    return map;
  }

  @PostMapping("/save")
  public Map<String, Object> postMethod(@RequestBody String content) {
    Map<String, Object> map = new HashMap<>();
    map.put("content", content);
    return map;
  }

  // 1. 语法 /cars/sell;low=34;brand=byd,audi,yd
  @GetMapping("/cars/{path}")
  public Map carSell(@MatrixVariable("low") Integer low,
      @MatrixVariable("brand") List<String> brand, @PathVariable("path") String path
  ) {
    Map<String, Object> map = new HashMap<>();
    map.put("low", low);
    map.put("bran", brand);
    map.put("path", path);
    return map;
  }

  // /boss/1;age=20/2;age=10
  @GetMapping("/boss/{bossId}/{empId}")
  public Map boss(@MatrixVariable(value = "age", pathVar = "bossId") Integer bossAge,
      @MatrixVariable(value = "age", pathVar = "empId") Integer empAge) {
    Map<String, Object> map = new HashMap<>();
    map.put("bossAge", bossAge);
    map.put("empAge", empAge);
    return map;
  }
}
