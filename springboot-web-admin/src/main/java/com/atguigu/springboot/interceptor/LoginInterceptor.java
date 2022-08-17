package com.atguigu.springboot.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 登陆检查 1. 配置好拦截器要拦截哪些请求 2. 把这些配置放在容器中
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    // 登陆检查逻辑
    HttpSession session = request.getSession();
    Object loginUser = session.getAttribute("loginUser");
    if (loginUser != null) {
      return true;
    }
    // 未找到登陆信息，拦截住 -> 跳转到登陆页
    String requestURI = request.getRequestURI();
    log.info("用户未登陆，拦截请求路径{}", requestURI);
    request.setAttribute("msg", "未登陆，请先登陆");
//    response.sendRedirect("/login");
    request.getRequestDispatcher("/").forward(request, response);
    return false;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
  }
}
