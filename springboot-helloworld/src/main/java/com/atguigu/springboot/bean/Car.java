package com.atguigu.springboot.bean;

import java.io.Serializable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 只有在容器中的组件，才会拥有SpringBoot提供的强大功能
 * 在application.properties中:
 * mycar.brand=Benz-Maybach
 * mycar.price=2000000
 */
@Component
@ConfigurationProperties(prefix = "mycar")
public class Car implements Serializable {

  private String brand;
  private Integer price;

  public Car() {
  }

  public Car(String brand, Integer price) {
    this.brand = brand;
    this.price = price;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "Car{" +
        "brand='" + brand + '\'' +
        ", price=" + price +
        '}';
  }
}
