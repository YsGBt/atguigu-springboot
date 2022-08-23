package com.atguigu.springboot.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 处理整个web的异常
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({ArithmeticException.class, NullPointerException.class}) // 处理异常
  public String handleArithmeticException(Exception exception) {
    log.error("异常是:{}", exception);
    return "login";
  }

}
