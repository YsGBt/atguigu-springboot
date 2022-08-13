package com.atguigu.springboot.bean;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person implements Serializable {

  private String userName;
  private Integer age;
  private Date birth;
  private Pet pet;
}
