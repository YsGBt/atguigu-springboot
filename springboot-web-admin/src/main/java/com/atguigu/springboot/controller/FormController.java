package com.atguigu.springboot.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

// 文件上传
@Controller
@Slf4j
public class FormController {

  @GetMapping("/form_layouts")
  public String formLayouts() {
    return "form/form_layouts";
  }

  /**
   * MultipartFile 自动封装上传过来的文件
   *
   * @param email
   * @param username
   * @param headerImg
   * @param photos
   * @return
   */
  @PostMapping("/upload")
  public String upload(@RequestParam("email") String email,
      @RequestParam("username") String username,
      @RequestPart(value = "headerImg", required = false) MultipartFile headerImg,
      @RequestPart(value = "photos", required = false) MultipartFile[] photos,
      HttpSession session) throws IOException {

    if (headerImg != null && photos != null) {
      log.info("上传的信息: email={}, username={}, headerImg={}, photos={}", email, username,
          headerImg.getSize(),
          photos.length);
    }

    ServletContext servletContext = null;

    if (headerImg != null && !headerImg.isEmpty()) {
      // 保存到文件服务器, OSS服务器
      // 获取上传文件的文件名
      String headerImgName = headerImg.getOriginalFilename();
      // 获取上传文件的后缀名
//      String suffix = filename.substring(filename.lastIndexOf("."));
      // 将UUID作为文件名
      String uuid = UUID.randomUUID().toString();
      // 将uuid和文件后缀名拼接后的结果作为最终的文件名
      headerImgName = uuid + headerImgName;
      // 通过ServletContext获取服务器中photo目录的路径
      servletContext = session.getServletContext();
      String headerImgPath = "/Users/qishenpang/Documents/GitHub/atguigu-springboot/springboot-web-admin/src/main/resources/static/upload/headerImg";
//      headerImgPath = servletContext.getRealPath("/static/upload/userHeadImg");
      File file = new File(headerImgPath);
      // 判断photoPath所对应路径是否存在
      if (!file.exists()) {
        // 若不存在，则创建目录
        file.mkdir();
      }
      String finalPath = headerImgPath + File.separator + headerImgName;
      headerImg.transferTo(new File(finalPath));
    }

    if (photos != null && photos.length > 0) {
      // 通过ServletContext获取服务器中photo目录的路径
      if (servletContext == null) {
        servletContext = session.getServletContext();
      }
      String photoImgPath = "/Users/qishenpang/Documents/GitHub/atguigu-springboot/springboot-web-admin/src/main/resources/static/upload/photos";
//      photoImgPath = servletContext.getRealPath("/static/upload/userPhotos");
      File file = new File(photoImgPath);
      if (!file.exists()) {
        file.mkdir();
      }
      for (MultipartFile photo : photos) {
        if (!photo.isEmpty()) {
          String photoName = photo.getOriginalFilename();
          String uuid = UUID.randomUUID().toString();
          photoName = uuid + photoName;
          String finalPath = photoImgPath + File.separator + photoName;
          photo.transferTo(new File(finalPath));
        }
      }
    }

    return "main";
  }
}
