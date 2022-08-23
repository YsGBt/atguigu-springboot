package com.atguigu.springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "用户数量太多")
public class TooManyUserException extends RuntimeException {

  public TooManyUserException() {
  }

  public TooManyUserException(String message) {
    super(message);
  }
}
