package com.atguigu.springboot.controller;

import com.atguigu.springboot.bean.User;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IndexController {

  // 访问登陆页
  @GetMapping(value = {"/", "/login"})
  public String loginPage() {
    return "login";
  }

  @PostMapping("/login")
  public String main(User user, HttpSession session, Model model) {
    // 这里密码写死为1
    if (StringUtils.hasLength(user.getUserName()) && "1".equals(user.getPassword())) {
      // 把登陆成功的用户保存起来
      session.setAttribute("loginUser", user);
      // 登陆成功重定向到main.html; 重定向防止表单重复提交
      return "redirect:/main.html";
    } else {
      model.addAttribute("msg", "账号密码错误");
      // 回到登陆页
      return "login";
    }
  }

  // 去main页面
  @GetMapping("/main.html")
  public String mainPage(HttpSession session, Model model) {
    // 是否登陆 (应该添加拦截器/过滤器来判断是否登陆)
//    Object user = session.getAttribute("loginUser");
//    if (user == null) {
//      model.addAttribute("msg", "请登陆");
//      return "login";
//    }
    return "main";
  }
}
