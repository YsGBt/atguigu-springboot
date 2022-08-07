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