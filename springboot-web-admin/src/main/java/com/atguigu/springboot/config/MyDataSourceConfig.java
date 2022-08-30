package com.atguigu.springboot.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import java.sql.SQLException;
import java.util.Arrays;
import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@Deprecated
//@Configuration
public class MyDataSourceConfig {

  // 默认的自动配置是判断容器中没有才会配 @ConditionalOnMissingBean(DataSource.class)
  @ConfigurationProperties("spring.datasource")
  @Bean
  public DataSource dataSource() throws SQLException {
    DruidDataSource druidDataSource = new DruidDataSource();
    // 加入防火墙和监控功能
    druidDataSource.setFilters("wall,stat");
    return druidDataSource;
  }

  // 配置 druid 的监控页功能
  @Bean
  public ServletRegistrationBean statViewServlet() {
    StatViewServlet statViewServlet = new StatViewServlet();
    ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<StatViewServlet>(
        statViewServlet, "/druid/*");
    servletRegistrationBean.addInitParameter("loginUsername", "admin");
    servletRegistrationBean.addInitParameter("loginPassword", "123");
    return servletRegistrationBean;
  }

  // WebStatFilter 用于采集 web-jdbc 关联监控的数据
  public FilterRegistrationBean webStatFilter() {
    WebStatFilter webStatFilter = new WebStatFilter();
    FilterRegistrationBean<WebStatFilter> webStatFilterFilterRegistrationBean = new FilterRegistrationBean<>(
        webStatFilter);
    webStatFilterFilterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
    webStatFilterFilterRegistrationBean.addInitParameter("exclusions",
        "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*</param-value>");
    return webStatFilterFilterRegistrationBean;
  }
}
