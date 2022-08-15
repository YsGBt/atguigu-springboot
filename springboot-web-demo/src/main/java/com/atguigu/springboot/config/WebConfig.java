package com.atguigu.springboot.config;

import com.atguigu.springboot.bean.Pet;
import com.atguigu.springboot.converter.GuiguMessageConverter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.accept.ParameterContentNegotiationStrategy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

@Configuration(proxyBeanMethods = false)
public class WebConfig /*implements WebMvcConfigurer*/ {

  @Bean
  public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
    HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
    hiddenHttpMethodFilter.setMethodParam("_m");
    return hiddenHttpMethodFilter;
  }

  @Bean
  public WebMvcConfigurer webMvcConfigurer() {
    WebMvcConfigurer webMvcConfigurer = new WebMvcConfigurer() {
      @Override
      public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
      }

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

      @Override
      public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new GuiguMessageConverter());
      }

      // 自定义内容协商策略
      @Override
      public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        Map<String, MediaType> mediaTypes = new HashMap<>();
        // 指定支持解析哪些参数对应的哪些媒体类型
        mediaTypes.put("json", MediaType.APPLICATION_JSON);
        mediaTypes.put("xml", MediaType.APPLICATION_XML);
        mediaTypes.put("gg", MediaType.parseMediaType("application/x-guigu"));
        ParameterContentNegotiationStrategy parameterStrategy = new ParameterContentNegotiationStrategy(
            mediaTypes);
        HeaderContentNegotiationStrategy headerStrategy = new HeaderContentNegotiationStrategy();
        configurer.strategies(Arrays.asList(parameterStrategy, headerStrategy));
      }
    };
    return webMvcConfigurer;
  }

//  @Override
//  public void configurePathMatch(PathMatchConfigurer configurer) {
//    UrlPathHelper urlPathHelper = new UrlPathHelper();
//    urlPathHelper.setRemoveSemicolonContent(false); // 设置不移除;后面内容 矩阵变量功能就可以生效
//    configurer.setUrlPathHelper(urlPathHelper);
//  }
}
