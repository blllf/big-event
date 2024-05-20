package com.blllf.bigevent.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.blllf.bigevent.mapper.UserMapper;
import com.blllf.bigevent.pojo.Result;
import com.blllf.bigevent.pojo.User;
import com.blllf.bigevent.util.AliOssUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@RestController
public class FileUploadController {

    @Autowired
    private UserMapper userMapper;

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

    @GetMapping("/fileExport")
    public void exportData(String fileName, HttpServletResponse response) {
        try {
            this.setExcelResponseProp(response, fileName);
            List<User> users = userMapper.selectList(null);
            //List<Student> list = CommonUtil.buildDemoExcel(Student.class);
            //使用 EasyExcel 库创建了一个 ExcelWriter 对象，指定了输出流为 HttpServletResponse 的输出流，这意呈现了响应的输出流。
            EasyExcel.write(response.getOutputStream())
                    .head(User.class)            //.head(ExcelFourDto.class) 设置 Excel 表格的表头信息
                    .excelType(ExcelTypeEnum.XLSX)  //.excelType(ExcelTypeEnum.XLSX) 指定了导出的 Excel 文件类型为 XLSX 格式
                    .sheet(fileName)                //.sheet(fileName) 设置了 Excel 的表单名为 fileName
                    .doWrite(users);             //.doWrite(list) 将数据写入 Excel 文件
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void setExcelResponseProp(HttpServletResponse response, String rawFileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode(rawFileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    }




}
