package com.atguigu.springboot.config;

import com.atguigu.springboot.bean.Pet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.HiddenHttpMethodFilter;
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
