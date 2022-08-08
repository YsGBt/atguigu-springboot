package com.atguigu.springboot.bean;

import java.io.Serializable;

public class Pet implements Serializable {

  private String name;

  public Pet() {
  }

  public Pet(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Pet{" +
        "name='" + name + '\'' +
        '}';
  }
}
