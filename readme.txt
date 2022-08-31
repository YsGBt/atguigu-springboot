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
            e. 虽然我们127个场景的所有自动配置启动的时候默认全部加载。xxxxAutoConfiguration
               按照条件装配规则（@Conditional），最终会按需配置。

      c) @ComponentScan 指定扫描包

   2) SpringBoot默认会在底层配好所有的组件, 但是如果用户自己配置了以用户的优先

   3) 总结
      - SpringBoot 先加载所有的自动配置类 xxxAutoConfiguration
      - 每个自动配置类按照条件进行生效，默认都会绑定配置文件指定的值 xxxProperties(xxxProperties和配置文件进行了绑定)
      - 生效的配置类就会给容器中装配很多组件
      - 只要容器中有这些组件，相当于这些功能就有了
      - 只要用户有自己配置的，就以用户的优先
      - 定制化配置
        - 用户直接自己 @Bean 替换底层的组件
        - 用户去看这个组件是获取的配置文件值，并进行修改
      - 整体流程:
        xxxAutoConfiguration -> 组件 -> xxxProperties里取值 -> application.properties中寻找

   4) 最佳实践
      - 引入场景依赖
      - 查看自动配置了哪些 (选做)
        - 自己分析，已引入场景的对应自动配置一般都生效了
        - 开启日志debug模式 (在application.properties中添加 debug=true)
      - 是否需要修改
        - 参照文档修改配置项/自己分析 xxxProperties 绑定的配置文件
        - 自定义加入或者替换组件 (@Bean @Component ...)
        - 自定义器 xxxCustomizer

4. 开发小技巧
   1) Lombok (在pom中配置唯一标识符即可，版本号SpringBoot已经管理)
      - 简化 JavaBean 开发
        @Data // getter setter toString hashcode
        @NoArgsConstructor
        @AllArgsConstructor
        @Slf4j // 用作日志输出 在类文件使用该注解后可以在类中使用 log 打印日志 例如 log.info("请求HelloWorld页面");

   2) dev-tools
      - 热更新 Ctrl + F9 重启应用
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-devtools</artifactId>
          <optional>true</optional>
      </dependency>

   3) Spring Initailizr (项目初始化向导)

5. Yaml (YAML Ain't Markup Language YAML 不是一种标记语言)
   - 非常适合用来做以数据为中心的配置文件
   - 推荐使用.yaml 取代 .properties
   - 注意SpringBoot会优先加载 .yaml 因此如果同时配置了 .properties 那么yaml与properties中重叠的内容会被properties覆盖

   1) 基本语法
      - key: value；kv之间有空格
      - 大小写敏感
      - 使用缩进表示层级关系
      - 缩进不允许使用tab，只允许空格
      - 缩进的空格数不重要，只要相同层级的元素左对齐即可
      - '#'表示注释
      - 字符串无需加引号，如果要加，''与"" 表示字符串内容 会被 不转义/转义
        - '' 不会转义 -> '\n' 会将 \n 作为字符串输出
        - "" 会转义 -> "\n" 会将 \n 作为换行输出

   2) 数据类型
      a) 字面量：单个的、不可再分的值。date、boolean、string、number、null
         k: v

      b) 对象：键值对的集合。map、hash、set、object
         行内写法：  k: {k1:v1,k2:v2,k3:v3}
         #或者
         k:
         	 k1: v1
           k2: v2
           k3: v3

      c) 数组：一组按次序排列的值。array、list、queue
         行内写法：  k: [v1,v2,v3]
         #或者
         k:
          - v1
          - v2
          - v3

   3) 示例
      person:
        userName: 张三
        boss: true
        birth: 2019/12/12 20:12:33
        age: 18
        pet:
          name: tomcat
          weight: 23.4
        interests: [ 篮球,足球 ]
        animal:
          - jerry
          - mario
        score:
          english:
            first: 30
            second: 40
            third: 50
          math: [ 131,140,148 ]
          chinese: { first: 128,second: 136 }
        salary: [ 99999,99999.98,99999.99 ]
        allPets:
          sick:
            - { name: tom }
            - { name: jerry,weight: 47 }
            - name: 阿猫
              weight: 88.88
          health: [ { name: mario,weight: 47 },{ name: 阿毛,weight: 47 } ]

   4) 配置 YAML 提示
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-configuration-processor</artifactId>
          <optional>true</optional>
      </dependency>

      - 注意在打包时排除此配置处理器，减少JVM负担 (SpringBoot 2.5以上版本会默认排除，不需要手动排除)
      <build>
          <plugins>
              <plugin>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-maven-plugin</artifactId>
                  <configuration>
                      <excludes>
                          <exclude>
                              <groupId>org.springframework.boot</groupId>
                              <artifactId>spring-boot-configuration-processor</artifactId>
                          </exclude>
                      </excludes>
                  </configuration>
              </plugin>
          </plugins>
      </build>

6. Web 开发
   1) 静态资源访问
      a) 只要静态资源放在类路径下: /static (or /public or /resources or /META-INF/resources)
        访问: 当前项目根路径/ + 静态资源名 ex. localhost:8080/pic.png
        改变默认的静态资源路径:
        spring.web.resources.static-locations=[classpath:/META-INF/resources/, classpath:/resources/, classpath:/static/, classpath:/public/]

      b) 原理: 静态映射 /**
        请求先通过DispatcherServlet，如果DispatcherServlet不能处理再交给静态资源处理器
        静态资源也找不到就会报404

      c) 静态资源访问前缀
         - 默认无前缀: spring.mvc.static-path-pattern=/**
         - 添加访问前缀
           spring:
             mvc:
               static-path-pattern: /res/**
           访问: 当前项目 + static-path-pattern + 静态资源名 = 静态资源文件夹下找

      d) webjar
         - 自动映射 /webjars/**
         - https://www.webjars.org/
         - 访问地址：http://localhost:8080/webjars/jquery/3.5.1/jquery.js 后面地址要按照依赖里面的包路径

   2) 欢迎页支持
      a) 静态路径下 index.html
         - 可以配置静态资源路径 spring.web.resources.static-locations
         - 但是不可以配置静态资源的访问前缀 spring.mvc.static-path-pattern. 否则导致 index.html不能被默认访问
      b) controller 处理 /index 请求

   3) 自定义 Favicon
      - favicon.ico 放在静态资源目录下即可

   4) 静态资源配置原理
      - SpringBoot启动默认加载 xxxAutoConfiguration 类 (自动配置类)
      - SpringMVC功能的自动配置类 WebMvcAutoConfiguration

      @Configuration(proxyBeanMethods = false)
      @ConditionalOnWebApplication(type = Type.SERVLET)
      @ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class })
      @ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
      @AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
      @AutoConfigureAfter({ DispatcherServletAutoConfiguration.class, TaskExecutionAutoConfiguration.class,
      		ValidationAutoConfiguration.class })
      public class WebMvcAutoConfiguration {}

      - WebMvcAutoConfiguration 给容器中配置了什么:
        @Configuration(proxyBeanMethods = false)
        @Import(EnableWebMvcConfiguration.class)
        @EnableConfigurationProperties({ WebMvcProperties.class, ResourceProperties.class })
        @Order(0)
        public static class WebMvcAutoConfigurationAdapter implements WebMvcConfigurer {}

      - 配置文件的相关属性和xxx进行了绑定
        WebMvcProperties==spring.mvc
        ResourceProperties==spring.resources

      a) 扩展: 当一个配置类只有一个有参构造器时
         - 有参构造器所有参数的值都会从容器中确定
         - ResourceProperties resourceProperties 获取和spring.resources绑定的所有的值的对象
         - WebMvcProperties mvcProperties 获取和spring.mvc绑定的所有的值的对象
         - ListableBeanFactory beanFactory Spring的beanFactory
         - HttpMessageConverters 找到所有的HttpMessageConverters
         - ResourceHandlerRegistrationCustomizer 找到 资源处理器的自定义器
         - DispatcherServletPath
         - ServletRegistrationBean 给应用注册Servlet、Filter....

          public WebMvcAutoConfigurationAdapter(ResourceProperties resourceProperties, WebMvcProperties mvcProperties,
                ListableBeanFactory beanFactory, ObjectProvider<HttpMessageConverters> messageConvertersProvider,
                ObjectProvider<ResourceHandlerRegistrationCustomizer> resourceHandlerRegistrationCustomizerProvider,
                ObjectProvider<DispatcherServletPath> dispatcherServletPath,
                ObjectProvider<ServletRegistrationBean<?>> servletRegistrations) {
              this.resourceProperties = resourceProperties;
              this.mvcProperties = mvcProperties;
              this.beanFactory = beanFactory;
              this.messageConvertersProvider = messageConvertersProvider;
              this.resourceHandlerRegistrationCustomizer = resourceHandlerRegistrationCustomizerProvider.getIfAvailable();
              this.dispatcherServletPath = dispatcherServletPath;
              this.servletRegistrations = servletRegistrations;
            }

      b) 资源处理的默认规则
         - spring.resources.addMappings=false -> 禁用所有静态资源规则 (默认true)

      c) 欢迎页的处理规则
         - 要使用欢迎页功能 (index.html) -> 必须使用 /**

   5) 请求参数处理
      a) 请求映射 (@xxxMapping)
         - Rest风格支持（使用HTTP请求方式动词来表示对资源的操作）
           - 以前: /getUser   获取用户     /deleteUser 删除用户    /editUser  修改用户       /saveUser 保存用户
           - 现在: /user    GET-获取用户    DELETE-删除用户     PUT-修改用户      POST-保存用户
           - 核心Filter；HiddenHttpMethodFilter
             - 用法: 表单method=post 隐藏域 _method=put
             - SpringBoot中手动开启 spring.mvc.hiddenmethod.filter=true
               @Bean
               @ConditionalOnMissingBean(HiddenHttpMethodFilter.class)
               @ConditionalOnProperty(prefix = "spring.mvc.hiddenmethod.filter", name = "enabled", matchIfMissing = false)
               public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
                 return new OrderedHiddenHttpMethodFilter();
               }

         - Rest原理（表单提交要使用REST的时候）
           - 表单提交会带上 _method=PUT
           - 请求过来被HiddenHttpMethodFilter拦截
             - 判断请求正常，并且是POST
               - 获取到_method的值
               - 兼容以下请求；PUT.DELETE.PATCH
               - 原生request(post)，包装模式requestWrapper重写了getMethod方法，返回的是传入的值
               - 过滤器链放行的时候用wrapper。以后的方法调用getMethod是调用requestWrapper的

         - Rest使用客户端工具，
           - 如PostMan直接发送 Put Delete 等方式请求，无需Filter

         - 扩展：如何把 _method 这个名字换成我们自己喜欢的
           //自定义filter
           @Bean
           public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
               HiddenHttpMethodFilter methodFilter = new HiddenHttpMethodFilter();
               methodFilter.setMethodParam("_m");
               return methodFilter;
           }

      b) 请求映射原理
         - SpringMVC功能分析都从 org.springframework.web.servlet.DispatcherServlet -> doDispatch()
           protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
           		HttpServletRequest processedRequest = request;
           		HandlerExecutionChain mappedHandler = null;
           		boolean multipartRequestParsed = false;

           		WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);

           		try {
           			ModelAndView mv = null;
           			Exception dispatchException = null;

           			try {
           				processedRequest = checkMultipart(request);
           				multipartRequestParsed = (processedRequest != request);

           				// 找到当前请求使用哪个Handler (Controller的方法)处理
           				mappedHandler = getHandler(processedRequest);
                  // HandlerMapping: 处理器映射 /xxx -> xxxx

         - RequestMappingHandlerMapping: 保存了所有@RequestMapping和handler的映射规则
         - 所有的请求映射都在 HandlerMapping中
           - SpringBoot自动配置欢迎页的 WelcomePageHandlerMapping 访问 '/' 能访问到index.html
           - SpringBoot自动配置了默认的 RequestMappingHandlerMapping
           - 请求进来，挨个尝试所有的 HandlerMapping 看是否有请求信息
             - 如果有就找到这个请求对应的 handler
             - 如果没有就是下一个 HandlerMapping
           - 我们需要一些自定义的映射处理，我们也可以自己给容器中放 HandlerMapping 自定义 HandlerMapping

         protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
           if (this.handlerMappings != null) {
             for (HandlerMapping mapping : this.handlerMappings) {
               HandlerExecutionChain handler = mapping.getHandler(request);
               if (handler != null) {
                 return handler;
               }
             }
           }
           return null;
         }

      c) 普通参数与基本注解
         1. 注解
            - @PathVariable
            - @RequestParam
            - @RequestHeader
            - @RequestAttribute
            - @CookieValue
            - @RequestBody
            - @ModelAttribute
            - @MatrixVariable
              - 注意: SpringBoot 默认是禁用了矩阵变量功能
                - 手动开始 (在@Configuration注解标注的类下):

                  @Bean
                  public WebMvcConfigurer webMvcConfigurer() {
                    WebMvcConfigurer webMvcConfigurer = new WebMvcConfigurer() {
                      @Override
                      public void configurePathMatch(PathMatchConfigurer configurer) {
                        UrlPathHelper urlPathHelper = new UrlPathHelper();
                        urlPathHelper.setRemoveSemicolonContent(false);
                        configurer.setUrlPathHelper(urlPathHelper);
                      }
                    };
                    return webMvcConfigurer;
                  }

              - queryString 查询字符串 (?): /cars/{path}?xxx=xxx&aaa=ccc
              - matrix 矩阵变量 (;): /cars/sell;low=34;brand=byd,audi,yd
                - 矩阵变量必须有url路径变量才能被解析
                - 例子:
                  // /boss/1;age=20/2;age=10
                  @GetMapping("/boss/{bossId}/{empId}")
                  public Map boss(@MatrixVariable(value = "age", pathVar = "bossId") Integer bossAge,
                      @MatrixVariable(value = "age", pathVar = "empId") Integer empAge) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("bossAge", bossAge);
                    map.put("empAge", empAge);
                    return map;
                  }
              - 页面开发，cookie禁用了，session里面的内容怎么使用
                - session.set(a,b) ---> jsessionid ---> cookie ----> 每次发请求携带
                - url重写: /abc;jsesssionid=xxxx 把cookie的值使用矩阵变量的方式进行传递

         2. Servlet API
            - SpringBoot 也允许方法传入ServletAPI来作为参数
            - ServletRequestMethodArgumentResolver  可以解析以下的部分参数
            - WebRequest ServletRequest MultipartRequest
              HttpSession javax.servlet.http.PushBuilder
              Principal InputStream Reader HttpMethod
              Locale TimeZone ZoneId

         3. 复杂参数
            - Map Model RedirectAttributes ServletResponse
            - Errors/BindingResult SessionStatus UriComponentsBuilder ServletUriComponentsBuilder

      d) POJO封装过程
         - 数据绑定: 页面提交的请求数据(Get, Post)都可以和对象属性进行绑定
         - ServletModelAttributeMethodProcessor 支持解析
         - WebDataBinder: web数据绑定器，将请求参数的值绑定到指定的JavaBean里面
         - WebDataBinder 利用它里面的 Converters 将请求数据转成指定的数据类型，再次封装到JavaBean中
         - GenericConversionService: 在设置每一个值的时候，找它里面的所有converter
                                     找到可以将这个数据类型(request带来的参数的字符串)转换到指定的类型
         - 可以给 WebDataBinder 里面放自己的Converter
           @Bean
           public WebMvcConfigurer webMvcConfigurer() {
            WebMvcConfigurer webMvcConfigurer = new WebMvcConfigurer() {
              @Override
              public void addFormatters(FormatterRegistry registry) {
                registry.addConverter(new Converter<String, Pet>() {
                  @Override
                  public Pet convert(String source) {
                    if (StringUtils.hasLength(source)) {
                      Pet pet = new Pet();
                      String[] split = source.split(",");
                      pet.setName(split[0]);
                      pet.setAge(Integer.parseInt(split[1]));
                      return pet;
                    }
                    return null;
                  }
                });
              }
            };
            return webMvcConfigurer;
           }

      e) 参数处理原理
         1. HandlerMapping中找到能处理请求的Handler (控制器方法)

         2. 为当前 Handler 找一个适配器 HandlerAdapter
            - 一共有四种 HandlerAdapter:
              a. RequestMappingHandlerAdapter - 支持方法上标注@RequestMapping注解的
              b. HandlerFunctionAdapter - 支持函数式编程的
              c. HttpRequestHandlerAdapter
              d. SimpleControllerHandlerAdapter

         3. 适配器执行目标方法并确定方法参数的每一个值，并执行方法
            a. DispatcherServlet 中 doDispatch:
               mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

            b. RequestMappingHandlerAdapter 中 handle -> handleInternal-> invokeHandlerMethod:
               mav = invokeHandlerMethod(request, response, handlerMethod); //执行目标方法

            c. HandlerMethodArgumentResolver 参数解析器 (ex. RequestParamMethodArgumentResolver, PathVariableMethodArgumentResolver)
               - 确定将要执行的目标方法的参数值
               - SpringMVC 目标方法能写多少中参数类型取决于参数解析器
               - 参数解析器内部方法:
                 1) supportsParameter(MethodParameter): boolean
                 2) resolverArgument(MethodParameter, ModelAndViewContainer,...): Object
               - 判断参数解析器是否支持解析当前参数，如果支持就调用resolverArgument进行解析

            d. ReturnValueHandler 返回值处理器 (ex. ModelAndViewMethodReturnValueHandler)

            e. RequestMappingHandlerAdapter 中 invokeHandlerMethod -> invokeAndHandle:
               invocableMethod.invokeAndHandle(webRequest, mavContainer);

            f. ServletInvocableHandlerMethod 中 invocableMethod -> invokeForRequest:
               // 真正执行目标方法并获取返回值
               Object returnValue = invokeForRequest(webRequest, mavContainer, providedArgs);

            g. InvocableHandlerMethod 中 invokeForRequest -> getMethodArgumentValues
               //获取方法的参数值
               Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);

         4. 目标方法执行完成
            a. 将所有的数据都放在 ModelAndViewContainer 包含要去的页面地址View 还包含Model数据

         5. 处理派发结果
            a. Dispatcher 中 doDispatch:
               processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);

            b. AbstractView:
               renderMergedOutputModel(mergedModel, getRequestToExpose(request), response);

            c. InternalResourceView:
               exposeModelAsRequestAttributes(model, request); //model中的所有数据遍历挨个放在请求域中

      f) 数据响应与内容协商
         1. 响应 JSON (jackson + @ResponseBody)
            - 引入 spring-boot-starter-web -> 自动引入 spring-boot-starter-json 依赖
            - jackson + @ResponseBody -> 给前端自动返回json数据

         2. ReturnValueHandlers 返回值解析器 (ex. ModelAndViewMethodReturnValueHandler, ResponseBodyEmitterReturnValueHandler)
            - supportsReturnType(methodParameter): boolean
            - handleReturnValue(Object, methodParameter, ...): Object
            - RequestResponseBodyMethodProcessor 可以处理返回值标了 @ResponseBody 注解的方法
              - 利用 MessageConverters 进行处理 将数据写为json
                - 内容协商 (浏览器默认会以请求头的方式告诉服务器它能接收什么样的内容类型)
                - 服务器最终根据自身的能力，决定服务器能生产出什么样的内容类型的数据
                - SpringMVC会挨个遍历所有容器底层的 HttpMessageConverter 看谁能处理
                  - 得到 MappingJackson2HttpMessageConverter 可以将对象写为json
                  - 利用 MappingJackson2HttpMessageConverter 将对象转为json再写出去

            @Override
            public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType,
                ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {

              HandlerMethodReturnValueHandler handler = selectHandler(returnValue, returnType);
              if (handler == null) {
                throw new IllegalArgumentException("Unknown return value type: " + returnType.getParameterType().getName());
              }
              handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
            }

         3. 返回值解析器原理
            a. 返回值处理器判断是否支持这种类型返回值 supportsReturnType
            b. 返回值处理器调用 handleReturnValue 进行处理
            c RequestResponseBodyMethodProcessor 可以处理返回值标了 @ResponseBody 注解的
              - 利用 MessageConverters 进行处理 将数据写为json
                1) 内容协商（浏览器默认会以请求头的方式告诉服务器他能接受什么样的内容类型）
                2) 服务器最终根据自己自身的能力，决定服务器能生产出什么样内容类型的数据
                3) SpringMVC会挨个遍历所有容器底层的 HttpMessageConverter 看谁能处理
                   - 得到MappingJackson2HttpMessageConverter可以将对象写为json
                   - 利用MappingJackson2HttpMessageConverter将对象转为json再写出去

         4. SpringMVC 支持的返回值类型
            - ModelAndView
            - Model
            - View
            - ResponseEntity
            - ResponseBodyEmitter
            - StreamingResponseBody
            - HttpEntity
            - HttpHeaders
            - Callable
            - DeferredResult
            - ListenableFuture
            - CompletionStage
            - WebAsyncTask
            - 有 @ModelAttribute 且为对象类型的
            - @ResponseBody 注解 -> RequestResponseBodyMethodProcessor

         5. 内容协商
            - 根据客户端接收能力的不同，返回不同的媒体类型数据

            a. 引入xml依赖
               <dependency>
                 <groupId>com.fasterxml.jackson.dataformat</groupId>
                 <artifactId>jackson-dataformat-xml</artifactId>
               </dependency>

            b. 内容协商原理
               1) 判断当前响应头中是否有确定的媒体类型 MediaType
               2) 获取客户端(PostMan，浏览器等)支持接收的内容类型 (获取客户端请求头Accept字段)
                  - 通过 contentNegotiationManager 内容协商管理器 默认使用基于请求头的策略
               3) 遍历循环所有当前系统的 MessageConverter，看谁支持这个对象 (Person)
               4) 找到支持操作Person的converter，把converter支持的媒体类型统计出来
               5) 客户端需要 application/xml 服务端能处理 application/json /+json /xml 等
               6) 进行内容协商的最佳匹配
               7) 用支持将对象转为最佳匹配媒体类型的converter来转化

         6. 开启浏览器参数方式内容协商功能
            - 为了方便内容协商，开启基于请求参数的内容协商功能
            - spring.contentnegotiation.favorParameter=true
            - 开启后客户端请求中当携带参数 format=xxx时 服务器会优先返回xxx格式的内容
              - ex. localhost:8080/test/person?format=xml -> 返回xml格式内容

         7. 自定义 MessageConverter
            a) 场景:
               - 浏览器发请求返回xml                [application/xml]   jacksonXmlConverter
               - ajax发请求返回json                [application/json]  jacksonJsonConverter
               - 如果硅谷app发请求返回自定义协议数据   [application/x-guigu] xxxConverter

            b) 步骤:
               1) 添加自定义的MessageConverter进系统底层
                  - 给容器中添加 WebMvcConfigurer 并实现方法 extendMessageConverters
                    @Override
                    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
                      converters.add(new GuiguMessageConverter());
                    }

               2) 系统底层就会统计出所有MessageConverter能操作哪些类型

               3) 客户端内容协商

               4) 自定义基于请求参数的内容协商供能 (有多种实现方法，这里展示的方法会覆盖spring原本的配置所以不推荐，更推荐使用yaml)
                  @Override
                  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
                    Map<String, MediaType> mediaTypes = new HashMap<>();
                    // 指定支持解析哪些参数对应的哪些媒体类型
                    mediaTypes.put("json", MediaType.APPLICATION_JSON);
                    mediaTypes.put("xml", MediaType.APPLICATION_XML);
                    mediaTypes.put("gg", MediaType.parseMediaType("application/x-guigu"));
                    ParameterContentNegotiationStrategy strategy = new ParameterContentNegotiationStrategy(
                        mediaTypes);
                    configurer.strategies(Arrays.asList(strategy));
                  }

   6) 视图解析与模版引擎
      - SpringBoot 默认不支持 JSP，需要引入第三方模版引擎技术实现页面渲染
      1. Thymeleaf
         表达式名字	    语法	        用途
         变量取值	    ${...}     	获取请求域、session域、对象等值
         选择变量	    *{...}	    获取上下文对象值
         消息	        #{...}	    获取Thymeleaf工具中的方法，文字消息表达式
         链接	        @{...}	    生成链接
         片段表达式	    ~{...}	    jsp:include 作用，引入公共页面片段

         a) 文本操作
            字符串拼接: +
            变量替换: |The name is ${name}|
         b) 布尔运算
            运算符:  and , or
            一元运算: ! , not
         c) 条件运算
            If-then: (if) ? (then)
            If-then-else: (if) ? (then) : (else)
            Default: (value) ?: (defaultValue)
         d) 遍历
            <tr class="gradeX" th:each="user,status:${users}">
              <td th:text="${status.count}">Trident</td>
              <td th:text="${user.userName}">Internet Explorer 4.0</td>
              <td th:text="${user.password}">Win 95+</td>
            </tr>

      2. Thymeleaf 使用
         a) 引入依赖 spring-boot-starter-thymeleaf
            <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-thymeleaf</artifactId>
            </dependency>

         b) SpringBoot 自动配置好 Thymeleaf
            @Configuration(proxyBeanMethods = false)
            @EnableConfigurationProperties(ThymeleafProperties.class)
            @ConditionalOnClass({ TemplateMode.class, SpringTemplateEngine.class })
            @AutoConfigureAfter({ WebMvcAutoConfiguration.class, WebFluxAutoConfiguration.class })
            public class ThymeleafAutoConfiguration { }

         c) 自动配好的策略
            - 所有Thymeleaf的配置都在 ThymeleafProperties
            - 配好了 SpringTemplateEngine
            - 配好了 ThymeleafViewResolver
            - 我们只需要直接开发页面
            - public static final String DEFAULT_PREFIX = "classpath:/templates/";
            - public static final String DEFAULT_SUFFIX = ".html"; // 默认跳转 xxx.html

         d) 页面开发
            - 引入名称空间
              <html lang="en" xmlns:th="http://www.thymeleaf.org">

      3. 视图解析 源码分析
         a) 目标方法处理的过程中，所有数据都会被放在 ModelAndViewContainer 里面 包括路径和视图地址
         b) 方法的参数是一个自定义类型对象(从请求参数中确定的)时，会被放到 ModelAndViewContainer 中
         c) 任何目标方法执行完成以后都会返回 ModelAndView (数据和视图地址)
         d) processDispatchResult 处理派发结果 (页面如何响应)
            1) render(mv, request, response); 进行页面渲染逻辑
               a. 根据方法的 String 返回值得到 View 对象 (定义了页面的渲染逻辑)
                  - 所有的视图解析器挨个尝试是否能根据当前返回值得到 View 对象
                  - 得到了 'redirect:/main.html' -> RedirectView
                  - ContentNegotiationViewResolver 里面包含了
                    BeanNameViewResolver, ThymeleafViewResolver, ViewResolverComposite, InternalResourceViewResolver
                    内部还是利用所包含的这些视图解析器得到视图对象
                  - view.render(mv.getModelInternal(), request, response); 视图对象调用自定义的render进行页面渲染工作
                    - RedirectView 渲染方式 (重定向到一个页面)
                      1. 获取目标url地址
                      2. response.sendRedirect(encodedURL);

            2) ThymeleafViewResolver 三种情况:
               a. 返回值以 forward: 开始: new InternalResourceView(forwardUrl); -> 转发
                  request.getRequestDispatcher(path).forward(request, response);
               b. 返回值以 redirect: 开始: new RedirectView() -> render 重定向
               c. 返回值是普通字符串: new ThymeleafView()

   7) 拦截器
      1. 拦截器使用
         a) 编写一个拦截器实现 HandlerInterceptor 接口
         b) 拦截器注册到容器中 (实现 WebConfigurer 的 addInterceptors)
         c) 指定拦截器规则 (在 addInterceptors 中) 如果是拦截所有'/**'需要注意静态资源也会被拦截

      2. 拦截器 源码分析
         a) 根据当前请求，找到 HandlerExecutionChain (可以处理请求的 handler 以及 handler 的所有拦截器)
         b) DispatcherServlet 中 doDispatch:
            if (!mappedHandler.applyPreHandle(processedRequest, response) return;
         c) 先按顺序执行所有拦截器的 preHandle 方法
            - 如果当前拦截器 preHandle 方法返回 true (放行) -> 则执行在一个拦截器的 preHandle
            - 如果当前拦截器返回为 false (拦截) -> 倒叙执行所有已经执行了的拦截器的 afterCompletion 方法
              -> 返回 false 并直接跳出 doDispatch 方法
         d) 如果所有拦截器放行 -> 执行目标方法 ha.handle(...);
         e) 按倒叙执行所有拦截器的 postHandle 方法
         f) 在上述步骤中有任何异常都会直接倒叙触发拦截器的 afterCompletion 方法
         g) 页面成功渲染完成后 倒叙触发拦截器的 afterCompletion 方法

   8) 文件上传
      1. html 页面
         <form role="form" th:action="@{/upload}" method="post" enctype="multipart/form-data">
            <div class="form-group">
              <label for="exampleInputFile">头像</label>
              <input type="file" name="headerImg" id="exampleInputFile">
            </div>
            <button type="submit" class="btn btn-primary">提交</button>
         </form>

      2. 文件上传代码
          @PostMapping("/upload")
           public String upload(@RequestPart(value = "headerImg", required = false) MultipartFile headerImg,
               HttpSession session) throws IOException {

             ServletContext servletContext = null;

             if (headerImg != null && !headerImg.isEmpty()) {
               // 保存到文件服务器, OSS服务器
               // 获取上传文件的文件名
               String headerImgName = headerImg.getOriginalFilename();
               // 将UUID作为文件名
               String uuid = UUID.randomUUID().toString();
               // 将uuid和文件后缀名拼接后的结果作为最终的文件名
               headerImgName = uuid + headerImgName;
               // 通过ServletContext获取服务器中photo目录的路径
               servletContext = session.getServletContext();
               String headerImgPath = servletContext.getRealPath("/static/upload/userHeadImg");
               File file = new File(headerImgPath);
               // 判断photoPath所对应路径是否存在
               if (!file.exists()) {
                 // 若不存在，则创建目录
                 file.mkdir();
               }
               String finalPath = headerImgPath + File.separator + headerImgName;
               headerImg.transferTo(new File(finalPath));
             }

             return "main";
           }

      3. 文件上传自动配置原理
         - 文件上传自动配置类 MultipartAutoConfiguration -> MultipartProperties
           - 自动配置好了 StandardServletMultipartResolver 文件上传解析器

         a) 客户端请求后 -> doDispatch -> checkMultipart
            使用文件上传解析器判断(isMultipart)并封装文件上传请求(将所有文件信息封装成Map并保存在新封装的请求中)
         b) 如果是文件上传请求 -> 文件上传解析器封装(resolveMultipart, 返回MultipartHttpServletRequest)文件上传请求
         c) 参数解析器 RequestPartMethodArgumentResolver 来解析请求中的文件内容并封装成 Multipart
         d) RequestPartMethodArgumentResolver 根据 @RequestPart的value 取出之前已经封装好的文件信息

   9) 异常处理
      1. 默认规则
         - 默认情况下，SpringBoot 提供 /error 处理所有错误的映射
         - 对于机器客户端，它将生成JSON响应，其中包含错误，HTTP状态和异常消息的详细信息
           对于浏览器客户端，响应一个 “whitelabel” 错误视图，以HTML格式呈现相同的数据
         - 要完全替换默认行为，可以实现 ErrorController 并注册该类型的Bean定义，
           或添加ErrorAttributes类型的组件以使用现有机制但替换其内容

         a) 自定义 error 页
            - error/404.html error/5xx.html 有精确的错误状态码页面就匹配精确，没有就找 4xx.html 如果都没有就触发白页
            - 自定义 404 错误页 / 5xx 错误页
              src/
               +- main/
                   +- java/
                   |   + <source code>
                   +- resources/
                       +- templates/
                           +- error/
                           |   +- 404.html
                           |   +- 5xx.html
                           +- <other public assets>

         b) 定制错误处理逻辑
            - @ControllerAdvice + @ExceptionHandler 处理全局异常 底层是 ExceptionHandlerExceptionResolver 支持的 (推荐)

            - @ResponseStatus + 自定义异常 底层是 ResponseStatusExceptionResolver
              把responsestatus注解的信息底层调用 response.sendError(statusCode, resolvedReason) tomcat发送的/error -> BasicErrorController

            - Spring底层的异常，如 参数类型转换异常；DefaultHandlerExceptionResolver 处理框架底层的异常
              - response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());

            - 自定义实现 HandlerExceptionResolver 处理异常 可以作为默认的全局异常处理规则
              默认优先度最低 因此需要在类上添加注解 @Order(value = Ordered.HIGHEST_PRECEDENCE + 10) // 优先级，数字越小优先级越高

            - ErrorViewResolver 实现自定义处理异常 (一般不使用)
              - response.sendError error请求就会转给controller
              - 你的异常没有任何HandlerExceptionResolver能处理 tomcat底层 response.sendError error请求就会转给controller
              - basicErrorController 要去的页面地址是 ErrorViewResolver

         c) 异常处理自动配置原理
            1) ErrorMvcAutoConfiguration 自动配置异常处理规则
               a. 容器中配置了组件 类型: DefaultErrorAttributes -> id: errorAttributes
                  - public class DefaultErrorAttributes implements ErrorAttributes, HandlerExceptionResolver
                  - DefaultErrorAttributes: 定义错误页面中可以包含哪些数据

               b. 容器中配置了组件 类型: BasicErrorController -> id: basicErrorController (json+白页 适配响应)
                  - 处理默认 /error 路径的请求 -> 页面响应 new ModelAndView("error", model); (注意这里的"error"就是返回视图，即下一行)
                  - 容器中配置了组件 类型: View -> id: error (响应默认错误页)
                  - 容器中配置了组件 类型: BeanNameViewResolver 视图解析器 (按照返回的视图名作为组件的id去容器中找View对象)
                    用来解析配置的 error View 视图
                  - 如果想要返回页面 -> 找error视图 (默认响应白页)

               c. 容器中配置了组件: 类型: DefaultErrorViewResolver -> id: conversionErrorViewResolver
                  - 如果发生错误，会以HTTP的状态码作为视图页地址(viewName)，找到真正的页面
                    - errorViewName = "error/" + viewName (ex."error/404" -> 响应 404.html 页面)

               d. 总结:
                  - 增加错误页面可以取到的值 -> 自定义 DefaultErrorAttributes
                  - 更改跳转逻辑 -> 自定义 BasicErrorController
                  - 更改错误页面路径 -> 自定义 DefaultErrorViewResolver

         d) 异常处理步骤流程
            1) 执行目标方法，目标方法运行期间有任何异常都会被 catch
               而且标志当前请求结束
               并且用 dispatchException 接收
            2) 进入视图解析流程 (页面渲染)
               processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
            3) 处理handler发生的异常 mv = processHandlerException()
               处理完成返回 ModelAndView
                a. 遍历所有的 handlerExceptionResolvers 看谁能处理异常 List<HandlerExceptionResolver>
                b. 系统默认的 处理器异常解析器:
                   - DefaultErrorAttributes: 先来处理异常 把异常信息保存到request域，并且返回null
                   - HandlerExceptionResolverComposite
                     - ExceptionHandlerExceptionResolver
                     - ResponseStatusExceptionResolver
                     - DefaultHandlerExceptionResolver
                c. 默认没有任何 处理器异常解析器 能够处理异常 -> 异常会被抛出
                d. 如果没有任何人能处理 -> 底层就会发送 /error 请求
                   会被底层的 BasicErrorController 处理
                e. 解析错误视图 遍历所有的 ErrorViewResolver 看谁能解析
                f. 默认的 DefaultErrorViewResolver -> 响应状态码作为错误页地址 (ex."error/404" -> 响应 404.html 页面)
                g. 模版引擎最终响应页面 error/404

   10) Web原生组件注入 (Servlet, Filter, Listener)
       a) 使用Servlet API (推荐)
          @ServletComponentScan(basePackages = "com.atguigu.springboot.servlet") -> 指定原生Servlet组件都放在哪里
          @WebServlet(urlPatterns = "/my") -> 声明Servlet，直接响应，没有经过 SpringMVC 拦截器
          @WebFilter(urlPatterns = {"/css/*", "/images/*"})
          @WebListener

       b) 使用 RegistrationBean (在com.atguigu.springboot.servlet.MyRegistrationConfig)
          - ServletRegistrationBean, FilterRegistrationBean, ServletListenerRegistrationBean

       c) 扩展: DispatchServlet 如何注册进来
          1) 容器中自动配置了 DispatchServlet 属性绑定到 WebMvcProperties -> @ConfigurationProperties(prefix="spring.mvc")
          2) 通过 ServletRegistrationBean<DispatchServlet> 把 DispatchServlet 配置进来 (默认映射 '/' 路径)
          3) Tomcat-Servlet: 如果多个Servlet都能处理到同一层路径，精确优先原则

   11) 嵌入式 Servlet 容器
       a) 切换嵌入式Servlet容器
          - 默认支持的 webServer
            - Tomcat, Jetty, or Undertow
            - ServletWebServerApplicationContext 容器启动寻找 ServletWebServerFactory 并引导创建服务器

          - 切换服务器
            - 先移除Tomcat场景
              <dependency>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-starter-web</artifactId>
                  <exclusions>
                      <exclusion>
                          <groupId>org.springframework.boot</groupId>
                          <artifactId>spring-boot-starter-tomcat</artifactId>
                      </exclusion>
                  </exclusions>
              </dependency>
            - 添加其他服务器场景 (ex.undertow)
              <dependency>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-starter-undertow</artifactId>
              </dependency>

          - 原理
            - SpringBoot应用启动发现当前是Web应用 web场景包-导入tomcat
            - web应用会创建一个web版的ioc容器 ServletWebServerApplicationContext
            - ServletWebServerApplicationContext  启动的时候寻找 ServletWebServerFactory
              (Servlet的web服务器工厂 -产出-> Servlet的web服务器)
            - SpringBoot底层默认有很多的WebServer工厂
              TomcatServletWebServerFactory, JettyServletWebServerFactory, or UndertowServletWebServerFactory
            - 底层直接会有一个自动配置类 ServletWebServerFactoryAutoConfiguration
            - ServletWebServerFactoryAutoConfiguration 导入了 ServletWebServerFactoryConfiguration (配置类)
            - ServletWebServerFactoryConfiguration 配置类 根据 @ConditionalOnClass 判断系统中到底导入了那个Web服务器的包
              (默认是web-starter导入tomcat包)，容器中就有 TomcatServletWebServerFactory
            - TomcatServletWebServerFactory 创建出Tomcat服务器并启动
              TomcatWebServer 的构造器拥有初始化方法 this.tomcat.start(); -> initialize();
            - 内嵌服务器，就是把手动启动服务器的代码调用 (tomcat核心jar包存在)

       b) 定制Servlet容器
          - 实现  WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
            - 把配置文件的值和 ServletWebServerFactory 进行绑定
          - 修改配置文件 server.xxx (推荐)
          - 直接自定义 ConfigurableServletWebServerFactory

   12) 定制化原理
       a) 定制化的常见方式
          - 修改配置文件
          - xxxxxCustomizer
          - 编写自定义的配置类 xxxConfiguration + @Bean替换、增加容器中默认组件 (ex.视图解析器)
          - Web应用 编写一个配置类实现 WebMvcConfigurer 即可定制化web功能 + @Bean给容器中再扩展一些组件
            @Configuration
            public class MyWebConfig implements WebMvcConfigurer {}
          - @EnableWebMvc + WebMvcConfigurer —— @Bean 可以全面接管SpringMVC，所有规则全部自己重新配置 实现定制和扩展功能
            - 原理
              1) WebMvcAutoConfiguration  默认的SpringMVC的自动配置功能类: 静态资源，欢迎页...
              2) 一旦使用 @EnableWebMvc 会 @Import(DelegatingWebMvcConfiguration.class)
              3) DelegatingWebMvcConfiguration 的作用，只保证SpringMVC最基本的使用
                 - 把所有系统中的 WebMvcConfigurer 拿过来 所有功能的定制都是IOC容器中所有的 WebMvcConfigurer 合起来一起生效
                 - 自动配置了一些非常底层的组件: RequestMappingHandlerMapping 这些组件依赖的组件都是从容器中获取
                 - public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport
              4) WebMvcAutoConfiguration 里面的配置要能生效 必须 @ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
              5) @EnableWebMvc 导致了 WebMvcAutoConfiguration 没有生效
       b) 原理分析套路
          - 场景starter -> xxxxAutoConfiguration -> 导入xxx组件 -> 绑定xxxProperties -> 绑定配置文件项

7. 数据访问
   1) SQL
      a) 数据源的自动配置
         1. 导入JDBC场景
            <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-data-jdbc</artifactId>
            </dependency>

            - spring-boot-starter-data-jdbc 导入了数据源 (HikariCP)，jdbc (spring-jdbc)，事务 (spring-tx)
            - 数据库驱动需要手动导入
              - SpringBoot已经设置好版本，如果需要引入具体版本 (两种方法)
                a. 直接依赖引入具体版本 (maven的就近依赖原则)
                b. 重新声明版本 (maven的属性的就近优先原则)
                   <properties>
                       <java.version>1.8</java.version>
                       <mysql.version>5.1.49</mysql.version>
                   </properties>

         2. 分析自动配置
            a. 自动配置的类
               - DataSourceAutoConfiguration: 数据的自动配置
                 - 修改配置源相关的配置: spring.datasource
                 - 数据库连接池的配置，是自己的容器中没有 DataSource 才自动配置
                 - SpringBoot 默认配置的连接池是 HikariDataSource

               - DataSourceTransactionManagerAutoConfiguration: 事务管理器的自动配置

               - JdbcTemplateAutoConfiguration: JdbcTemplate 的自动配置，可以来对数据库进行crud
                 - 通过修改 spring.jdbc 来修改 JdbcTemplate 的自动配置

               - JndiDataSourceAutoConfiguration: Jndi的自动配置

               - XADataSourceAutoConfiguration: 分布式事务相关的

         3. 修改配置项
            - 配置数据库连接所需要的参数
              spring:
                datasource:
                  url: jdbc:mysql://localhost:3306/mybatis?characterEncoding=UTF-8
                  username: javaConnection
                  password: tztlx13GBT
                  driver-class-name: com.mysql.cj.jdbc.Driver

      b) 使用 Druid 数据源
         1. 通过自定义的方式使用 Druid
            @Configuration
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

         2. 通过 starter 方式使用 Druid
            a. 引入 druid-starter
               <dependency>
                   <groupId>com.alibaba</groupId>
                   <artifactId>druid-spring-boot-starter</artifactId>
                   <version>1.1.17</version>
               </dependency>

            b. 分析自动配置
               - 扩展配置项 spring.datasource.druid
               - DruidSpringAopConfiguration.class 监控SpringBean的配置项: spring.datasource.druid.aop-patterns
               - DruidStatViewServletConfiguration.class 监控页的配置: spring.datasource.druid.stat-view-servlet 默认不开启
               - DruidWebStatFilterConfiguration.class web监控配置: spring.datasource.druid.web-stat-filter 默认不开启
               - DruidFilterConfiguration.class 所有Druid自己filter的配置

            c. 官方文档 https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter

      c) 整合 MyBatis 操作
         - 导入mybatis-starter -> 编写mapper接口 -> 编写sql映射文件并绑定mapper接口(注意标注@Mapper) -> 在application.yaml中指定mapper配置文件的位置以及全局配置文件的信息(mybatis.configuration)
         a. 引入 MyBatis Starter
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>2.1.4</version>
            </dependency>

         b. 分析自动配置
            @EnableConfigurationProperties(MybatisProperties.class) // MyBatis配置项绑定类
            @AutoConfigureAfter({ DataSourceAutoConfiguration.class, MybatisLanguageDriverAutoConfiguration.class })
            public class MybatisAutoConfiguration {}

            @ConfigurationProperties(prefix = "mybatis")
            public class MybatisProperties

            - SqlSessionFactory: 自动配置好了
            - SqlSession: 自动配置了 SqlSessionTemplate (类里拥有SqlSession参数)
            - Mapper: 只要我们写的操作MyBatis的接口标注了@Mapper就会被自动扫描进来

         c. 可以通过xml来配置mybatis，或者也可以通过yaml中的mybatis.configuration属性来配置
            - 可以不写全局配置文件mybatis-config.xml，所有全局配置文件的配置都放在configuration配置项中即可

         d. 纯注解方式 (不常用)
            - 可以xml/注解混用

            - 使用注解
              @Select("select * from t_employee where eid=#{id}")
              Employee getEmployeeByAnnotation(@Param("id") Integer id);

            - insert返回主键
              @Options(useGeneratedKeys = true, keyProperty = "eid")

         e. 最佳实战
            - 引入mybatis-starter
            - 在application.yaml中，指定mapper-location位置
            - 编写Mapper接口并标注@Mapper注解 (或者使用 @MapperScan("com.atguigu.springboot.mapper") 简化)
            - 简单方法直接注解方式
            - 复杂方法编写mapper.xml进行绑定映射

      d) 整合 MyBatis-Plus 完成 CRUD
         - 官网 https://baomidou.com/
         a. 引入 mybatis-plus-boot-starter
            <dependency>
              <groupId>com.baomidou</groupId>
              <artifactId>mybatis-plus-boot-starter</artifactId>
              <version>3.4.1</version>
            </dependency>

         b. 分析自动配置
            - MybatisPlusAutoConfiguration 配置类
            - MybatisPlusProperties 配置项绑定 mybatis-plus: xxx 就是对mybatis-plus的定制
            - SqlSessionFactory: 自动配置好，底层是容器中默认的数据源
            - MapperLocations: 配置好默认值: classpath*:/mapper/**/*.xml;
              任意包的类路径下的所有mapper文件夹下任意路径下的所有xml都是sql映射文件 (建议以后sql映射文件放在mapper下)
            - SqlSessionTemplate: 容器中也自动配置好了SqlSessionTemplate
            - @Mapper 标注的接口也会被自动扫描

         c. 使用
            1) 使用MybatisPlus只需要我们的Mapper继承BaseMapper就可以拥有CRUD能力
              - 创建Mapper
              @Mapper
              public interface MBPUserMapper extends BaseMapper<MBPUser> {}

              - Pojo类
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

              - 直接使用
                @Test
                void testUserMapper() {
                  MBPUser user = userMapper.selectById(1L);
                  log.info("用户信息: {}", user);
                }

            2) Service类 MybatisPlus也有实现，需要继承IService类
              - 接口
                public interface UserService extends IService<MBPUser> {
                }

              - 实现类
                @Service
                public class UserServiceImpl extends ServiceImpl<MBPUserMapper, MBPUser> implements UserService {
                }

