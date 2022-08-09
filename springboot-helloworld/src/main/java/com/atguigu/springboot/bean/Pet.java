package com.atguigu.springboot.bean;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pet implements Serializable {

  private String name;
  private Double weight;

  public Pet(String name) {
    this.name = name;
  }
}
