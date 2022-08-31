package com.atguigu.springboot.springbootwebadmin;

import com.atguigu.springboot.bean.MBPUser;
import com.atguigu.springboot.mapper.MBPUserMapper;
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

  @Autowired
  private MBPUserMapper userMapper;

  @Test
  void contextLoads() {
    Long query = jdbcTemplate.queryForObject("select count(*) from t_user", Long.class);
    log.info("记录总数: {}", query);
  }

  @Test
  void testUserMapper() {
    MBPUser user = userMapper.selectById(1L);
    log.info("用户信息: {}", user);
  }

}
