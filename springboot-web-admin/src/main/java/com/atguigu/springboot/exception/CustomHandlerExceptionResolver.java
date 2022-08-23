package com.atguigu.springboot.exception;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE + 10) // 优先级，数字越小优先级越高
public class CustomHandlerExceptionResolver implements HandlerExceptionResolver {

  @Override
  public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) {

    try {
      response.sendError(511, "自定义错误");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new ModelAndView();
  }
}
