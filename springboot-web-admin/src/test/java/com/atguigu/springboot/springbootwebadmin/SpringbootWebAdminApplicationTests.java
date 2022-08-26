package com.atguigu.springboot.springbootwebadmin;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@SpringBootTest
class SpringbootWebAdminApplicationTests {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void contextLoads() {
    Long query = jdbcTemplate.queryForObject("select count(*) from t_user", Long.class);
    log.info("记录总数: {}", query);
  }

}
