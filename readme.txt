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
            a) 所有的 MessageConverter






