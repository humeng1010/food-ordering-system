package com.food.controller;

import com.food.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * 文件的上传和下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${upload-img.path}")
    private String basePath;
    /**
     * 参数的名字必须和前端上传的input的name属性值一致
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        try {
//            防止目录不存在
            File file1 = new File(basePath);
            if (!file1.exists()){
                file1.mkdirs();
            }

            String uuid = UUID.randomUUID().toString();
            String originalFilename = file.getOriginalFilename();

            assert originalFilename != null;
            // 获取文件后缀 带 .
            String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = uuid + substring;
//            文件转储
            file.transferTo(new File(basePath + fileName));
            return Result.success(fileName);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/download")
    public void download(@RequestParam String name, HttpServletResponse response){
        try (
                FileInputStream fis = new FileInputStream(basePath + name);
                BufferedInputStream bis = new BufferedInputStream(fis)
        ) {
            response.reset();
            ServletOutputStream outputStream = response.getOutputStream();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            response.setContentType("image/jpeg");

            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer))!=-1){
                bufferedOutputStream.write(buffer,0,len);
                bufferedOutputStream.flush();
            }

            bufferedOutputStream.close();
            outputStream.close();

        }catch (Exception e){
            throw new RuntimeException();
        }
    }
}
