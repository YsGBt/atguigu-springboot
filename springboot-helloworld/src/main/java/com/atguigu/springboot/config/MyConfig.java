package com.atguigu.springboot.config;

import com.atguigu.springboot.bean.Pet;
import com.atguigu.springboot.bean.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * 1. 配置类里面使用 @Bean 标注在方法上给容器注册组件，默认也是单实例的
 * 2. 配置类本身也是组件
 * 3. proxyBeanMethods: 代理bean的方法
 *    - Full(proxyBeanMethods = true) 保证每个@Bean方法被调用多少次返回的组件都是单实例的
 *    - 配置类组件之间有依赖关系，方法会被调用得到之前单实例组件，用Full模式
 *    - Lite(proxyBeanMethods = false) 每个@Bean方法被调用多少次返回的组件都是新创建的
 *    - 配置类组件之间无依赖关系用Lite模式加速容器启动过程，减少判断
 * 4. @Import({User.class, DBHelper.class})  给容器中自动创建出这两个类型的组件，默认组件名是全类名
 * 5. @ImportResource("classpath:beans.xml") 导入xml配置
 */
@Import({User.class})
@ImportResource("classpath:beans.xml")
@Configuration(proxyBeanMethods = true) // 告诉SpringBoot这是一个配置类 == 配置文件
//@ConditionalOnMissingBean(name = "tom")
//@EnableConfigurationProperties(Car.class) // 1. 开启Car的属性配置绑定功能 2. 把Car这个组件自动注册到容器中 (这样如果使用第三方包就不需要@Component了)
public class MyConfig {

  /**
   * 外部无论对配置类中的这个组件注册方法调用多少次获取的都是之前注册到容器中的单实例对象
   * @return
   */
  @Bean // 给容器中添加组件，以方法名作为组件的id ('user01')，返回类型就是组件类型，返回的值就是组件在容器中的实例
  public User user01() {
    User user = new User("张三", 18);
    // user组件依赖的pet组件
    user.setPet(tomcatPet());
    return user;
  }

  @Bean("tom")
  public Pet tomcatPet() {
    return new Pet("tomcat");
  }

  @ConditionalOnMissingBean(name = "tom")
  @Bean
  public Pet petercat() {
    return new Pet("peter");
  }
}
