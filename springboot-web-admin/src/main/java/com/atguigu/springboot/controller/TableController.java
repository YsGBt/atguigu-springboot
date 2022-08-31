package com.atguigu.springboot.controller;

import com.atguigu.springboot.bean.MBPUser;
import com.atguigu.springboot.exception.TooManyUserException;
import com.atguigu.springboot.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TableController {

  @Autowired
  private UserService userService;

  @GetMapping("/basic_table")
  public String basic_table() {
    return "table/basic_table";
  }

  @GetMapping("/user/delete/{id}")
  public String deleteUser(@PathVariable("id") Long id,
      @RequestParam(value = "page", defaultValue = "1") Integer page,
      RedirectAttributes redirectAttributes) {
    userService.removeById(id);

    // 重定向携带参数
    redirectAttributes.addAttribute("page", page);

    return "redirect:/dynamic_table";
  }

  @GetMapping("/dynamic_table")
  public String dynamic_table(@RequestParam(value = "page", defaultValue = "1") Integer page,
      Model model) {
    // 表格内容的遍历
//    List<User> users = Arrays.asList(new User("张三", "123456"),
//        new User("lisa", "123444"),
//        new User("haha", "aaaa"),
//        new User("nana", "bbbb"));
//
//    model.addAttribute("users", users);

    // 从数据库中查出user表中的内容
//    List<MBPUser> list = userService.list();
//    model.addAttribute("users", list);

    // 分页查询数据
    Page<MBPUser> mbpUserPage = new Page<>(page, 2);

    // 分页查询的结果
    Page<MBPUser> queryUsers = userService.page(mbpUserPage, null);

    model.addAttribute("page", queryUsers);

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
