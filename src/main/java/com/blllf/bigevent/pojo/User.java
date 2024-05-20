package com.blllf.bigevent.pojo;



import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @NotNull
    @ExcelProperty("用户ID")
    @ColumnWidth(20)
    private Integer id;//主键ID

    @ExcelProperty("用户名")
    @ColumnWidth(20)
    private String username;//用户名

    @ExcelProperty("密码")
    @ColumnWidth(20)
    //@JsonIgnore //Springmvc 把当前对象转换为JSON数据的时候忽略password，最终JSON数据中没有这个属性
    private String password;//密码

    @Pattern(regexp = "^\\S{1,20}$")
    @ExcelProperty("昵称")
    @ColumnWidth(20)
    private String nickname;//昵称

    @NotEmpty
    @Email
    @ExcelProperty("邮箱")
    @ColumnWidth(20)
    private String email;//邮箱

    @ExcelProperty("头像url")
    @ColumnWidth(20)
    private String userPic;//用户头像地址

    @ExcelProperty("创建时间")
    @ColumnWidth(20)
    private LocalDateTime createTime;//创建时间

    @ExcelProperty("更新时间")
    @ColumnWidth(20)
    private LocalDateTime updateTime;//更新时间
}
