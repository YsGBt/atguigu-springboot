package com.atguigu.springboot.mapper;

import com.atguigu.springboot.bean.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

  Employee getEmployeeById(@Param("id") Integer id);

  @Select("select * from t_employee where eid=#{id}")
  Employee getEmployeeByAnnotation(@Param("id") Integer id);
}
