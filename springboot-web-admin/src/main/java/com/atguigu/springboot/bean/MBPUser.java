package com.atguigu.springboot.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// 设置数据库映射table名
@TableName("user")
public class MBPUser {

  private Long id;
  private String name;
  private Integer age;
  private String email;

  // 默认所有属性都应该在数据库中，如果属性不在数据库中则需要标注此属性不存在
  @TableField(exist = false)
  private Employee employee;
}
