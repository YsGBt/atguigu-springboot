package com.atguigu.springboot;

import com.atguigu.springboot.bean.Pet;
import com.atguigu.springboot.bean.User;
import com.atguigu.springboot.config.MyConfig;
import java.util.Arrays;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 主程序类;主配置类
 *
 * @SpringBootApplication: 这是一个SpringBoot应用
 * @SpringBootApplication 等同于
 * @SpringBootConfiguration
 * @EnableAutoConfiguration
 * @ComponentScan("com.atguigu.springboot")
 */
@SpringBootApplication
public class MainApplication {

  public static void main(String[] args) {
    // 1. 返回IOC容器
    ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);

    // 2. 查看容器里面的组件
    String[] names = run.getBeanDefinitionNames();
    Arrays.stream(names).forEach(System.out::println);

    // 3. 从容器中获取组件 (组件默认是单实例的)
    Pet tom01 = run.getBean("tom", Pet.class);
    Pet tom02 = run.getBean("tom", Pet.class);
    System.out.println("组件: " + (tom01 == tom02));

    // 4. com.atguigu.springboot.config.MyConfig$$EnhancerBySpringCGLIB$$4492b90@7c2327fa
    MyConfig bean = run.getBean(MyConfig.class);
    System.out.println("bean = " + bean);

    // 如果 @Configuration(proxyBeanMethods = true) 代理对象调用方法。SpringBoot总会检查这个组件是否在容器中有。
    // 保持组件单实例
    User user01 = bean.user01();
    User user02 = bean.user01();
    System.out.println(user01 == user02); // --> true

    User user = run.getBean("user01", User.class);
    Pet tom = run.getBean("tom", Pet.class);
    System.out.println("用户的宠物: " + (user.getPet() == tom));

    // 5. 获取组件
    String[] beanNamesForType = run.getBeanNamesForType(User.class);
    for (String s : beanNamesForType) {
      System.out.println(s);
    }

    boolean xmlUser = run.containsBean("xmlUser");
    System.out.println("contains xmlUser = " + xmlUser);
  }
}
