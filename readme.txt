https://www.yuque.com/atguigu/springboot
1. SpringBoot 入门
   1) 搭建 SpringBoot 项目
      a) 在 maven pom 文件中添加 springboot 父项目做依赖管理

         <parent>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-parent</artifactId>
           <version>2.7.2</version>
         </parent>

      b) 引入 starter 依赖

         <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-web</artifactId>
         </dependency>

      c) 创建主程序

         /**
          * 主程序类
          * @SpringBootApplication: 这是一个SpringBoot应用
          * 等同于
          * @SpringBootConfiguration
          * @EnableAutoConfiguration
          * @ComponentScan("com.atguigu.springboot")
          */
         @SpringBootApplication
         public class MainApplication {

           public static void main(String[] args) {
             SpringApplication.run(MainApplication.class, args);
           }
         }

      d) 编写业务

         @RestController
         public class HelloController {

           @RequestMapping("/hello")
           public String hello() {
             return "Hello, Spring Boot!";
           }
         }

      e) 测试
         - 直接运行 主程序 中的 main 方法

      f) 简化配置
         - 在resources目录下添加: application.properties

      g) 简化部署
         - 把项目打成jar包，直接在目标服务器执行即可。
         <build>
           <plugins>
             <plugin>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-maven-plugin</artifactId>
             </plugin>
           </plugins>
         </build>

   2) 了解自动配置原理
      a) 依赖管理
         - 父项目做依赖管理
           <parent>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-parent</artifactId>
             <version>2.3.4.RELEASE</version>
           </parent>

         - spring-boot-starter-parent的父项目:
            <parent>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-dependencies</artifactId>
               <version>2.3.4.RELEASE</version>
            </parent>
            几乎声明了所有开发中常用的依赖的版本号,自动版本仲裁机制

         - 修改默认版本号
           1. 查看spring-boot-dependencies里面规定当前依赖的版本用的 key
           2. 在当前项目里面重写配置
           <properties>
               <mysql.version>5.1.43</mysql.version>
           </properties>

         - 开发导入starter场景启动器
           1. spring-boot-starter-* ： *代表某种场景
           2. 只要引入starter，这个场景的所有常规需要的依赖都会自动引入
           3. SpringBoot所有支持的场景
              https://docs.spring.io/spring-boot/docs/current/reference/html/using-spring-boot.html#using-boot-starter
           4. *-spring-boot-starter: 代表第三方为我们提供的简化开发的场景启动器。
           5. 所有场景启动器最底层的依赖
           <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter</artifactId>
             <version>2.3.4.RELEASE</version>
             <scope>compile</scope>
           </dependency>

      2) 自动配置
         a) 自动配好 Tomcat
            - 自动引入Tomcat依赖并且配置好Tomcat
         b) 自动配好 SpringMVC
            - 自动引入SpringMVC全套组件，并且自动配好常用组件
         c) 自动配好Web常见功能，如：字符编码问题
            - SpringBoot帮我们配置好了所有web开发的常见场景
         d) 默认的包结构
            - 主程序所在的包以及其下面的所有子包里面的组件都会被默认扫描进来
            - 无需以前的包扫描配置
            - 想要改变扫描路径，@SpringBootApplication(scanBasePackages="com.atguigu")
                             或者@ComponentScan 指定扫描路径
            @SpringBootApplication
            等同于
            @SpringBootConfiguration
            @EnableAutoConfiguration
            @ComponentScan("com.atguigu.boot")
         e) 各种配置拥有默认值
            - 默认配置最终都是映射到某个类上，如：MultipartProperties
            - 配置文件的值最终会绑定某个类上，这个类会在容器中创建对象
         f) 按需加载所有自动配置项
            - 引入了哪些场景这个场景的自动配置(starter)才会开启
            - SpringBoot所有的自动配置功能都在 spring-boot-autoconfigure 包里面

2. 容器功能
   1) @Configuration

   2) @Bean @Component @Controller @Service @Repository

   3) @ComponentScan @Import

   4) @Conditional
      - 条件装配：满足Conditional指定的条件，则进行组件注入

   5) @ImportResource

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

   6) 配置绑定
      - 如何使用Java读取到properties文件中的内容，并且把它封装到JavaBean中，以供随时使用

      a) @Component + @ConfigurationProperties
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
         }

      b) @EnableConfigurationProperties + @ConfigurationProperties
         - 在配置类(@Configuration)中:
         - @EnableConfigurationProperties(Car.class)
         - 1. 开启Car的属性配置绑定功能
         - 2. 把Car这个组件自动注册到容器中 (这样如果使用第三方包就不需要@Component了)

3. 自动配置原理
   1) @SpringBootApplication 引导加载自动配置类:

      @SpringBootConfiguration
      @EnableAutoConfiguration
      @ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
          @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
      public @interface SpringBootApplication{}

      a) @SpringBootConfiguration == @Configuration 代表当前是一个配置类

      b) @EnableAutoConfiguration :

         @AutoConfigurationPackage
         @Import(AutoConfigurationImportSelector.class)
         public @interface EnableAutoConfiguration {}

         1. @AutoConfigurationPackage 自动配置包
            - 利用Registrar给容器中导入一系列组件
            - 将MainApplication包下的所有组件导入进来

            @Import(AutoConfigurationPackages.Registrar.class)  //给容器中导入一个组件
            public @interface AutoConfigurationPackage {}

         2. @Import(AutoConfigurationImportSelector.class)
            a. 利用getAutoConfigurationEntry(annotationMetadata) 给容器中批量导入一些组件
            b. 调用List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes) 获取到所有需要导入到容器中的配置类
            c. 利用工厂加载 Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) 得到所有的组件
            d. 从META-INF/spring.factories位置来加载一个文件
            	 默认扫描我们当前系统里面所有META-INF/spring.factories位置的文件
                 spring-boot-autoconfigure-2.3.4.RELEASE.jar包里面也有META-INF/spring.factories


      c) @ComponentScan 指定扫描包
