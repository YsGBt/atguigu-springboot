package com.atguigu.springboot.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

//@Component
public class RedisUrlCountInterceptor implements HandlerInterceptor {

//  @Autowired
  public StringRedisTemplate stringRedisTemplate;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String uri = request.getRequestURI();

    // 默认每次访问当前uri就会计数+1
    stringRedisTemplate.opsForValue().increment(uri);

    return true;
  }
}
