package com.blllf.bigevent.controller;

import com.blllf.bigevent.pojo.Result;
import com.blllf.bigevent.util.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {

        String originalFilename = file.getOriginalFilename();
        //保证文件名唯一
        String filename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        //把文件存储到本地磁盘中
        // file.transferTo(new File("C:\\Users\\lovinyq\\Desktop\\file\\" + filename));

        String url = AliOssUtil.upload(filename, file.getInputStream());

        return Result.success(url);

    }
}
