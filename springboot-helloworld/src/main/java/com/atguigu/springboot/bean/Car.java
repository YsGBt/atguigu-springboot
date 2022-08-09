package com.atguigu.springboot.bean;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 只有在容器中的组件，才会拥有SpringBoot提供的强大功能
 * 在application.properties中:
 * mycar.brand=Benz-Maybach
 * mycar.price=2000000
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "mycar")
public class Car implements Serializable {

  private String brand;
  private Integer price;
}
