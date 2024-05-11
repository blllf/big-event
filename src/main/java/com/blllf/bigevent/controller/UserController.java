package com.blllf.bigevent.controller;


import com.blllf.bigevent.pojo.Result;
import com.blllf.bigevent.pojo.User;
import com.blllf.bigevent.service.UserService;
import com.blllf.bigevent.util.Captcha;
import com.blllf.bigevent.util.JwtUtil;
import com.blllf.bigevent.util.Md5Util;
import com.blllf.bigevent.util.ThreadLocalUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    private static String verifyCode ;

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private JavaMailSender javaMailSender;


    @PostMapping("/register")
    //@GetMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$")String username , @Pattern(regexp = "^\\S{5,16}$")String password){
        User u = userService.findByUsername(username);

        if (u == null){
            //数据库没有相同的用户
            userService.register(username , password);
            return Result.success();
        }else {
            return Result.error("用户名已被占用");
        }
    }

    //@RequestMapping("/login")
    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$")String username , @Pattern(regexp = "^\\S{5,16}$")String password ,
                                String remember , HttpServletResponse response){
        User loginUser = userService.findByUsername(username);
        if (loginUser==null){
            return Result.error("用户名错误");
        }
        Boolean flag = Boolean.valueOf(remember);

        if (flag){
            //System.out.println("remember:" + flag);
            Cookie c_username = new Cookie("username" , username);
            Cookie c_password = new Cookie("password" , password);

            c_username.setMaxAge(60 * 60 * 24 * 7);
            c_password.setMaxAge(60 * 60 * 24 * 7);

            response.addCookie(c_username);
            response.addCookie(c_password);
        }

        //System.out.println(flag);

        if (Md5Util.getMD5String(password).equals(loginUser.getPassword())){
            //登录成功
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("id" , loginUser.getId());
            claims.put("username" , loginUser.getUsername());
            String token = JwtUtil.genToken(claims);
            //把token 存到redis中
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,12, TimeUnit.HOURS);
            return Result.success(token);
        }

        return Result.error("密码错误");
    }

    //查询出所有用户
    @GetMapping("/selectAllusers")
    public Result<List<User>> selectAll(){
        List<User> users = userService.selectAll();
        return Result.success(users);
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo(){
        //@RequestHeader(name = "Authorization") String token
        //每次都要获取一次令牌，从令牌中获得用户的信息，可以用ThreadLocal对象存储用户信息并获取
        Map<String , Object> map = ThreadLocalUtil.get();

        String username = (String) map.get("username");

        /*Map<String, Object> map = JwtUtil.parseToken(token);
        String username = (String) map.get("username");*/

        User u = userService.findByUsername(username);

        return Result.success(u);

    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user){

        Boolean flag = userService.update(user);

        if (flag){
            return Result.success();
        }
        return Result.error("邮箱已存在");
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePassword")
    public Result updatePassword(@RequestBody Map<String , String> map , @RequestHeader("Authorization") String token){
        String oldPwd = map.get("old_pwd");
        String newPwd = map.get("new_pwd");
        String rePwd = map.get("re_pwd");

        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error("缺少必要的参数");
        }

        //原密码是否正确
        Map<String , Object> userMap = ThreadLocalUtil.get();
        String username = (String) userMap.get("username");
        User u = userService.findByUsername(username);
        if (!u.getPassword().equals(Md5Util.getMD5String(oldPwd))){
            return Result.error("原密码填写错误");
        }

        //newPwd与rePwd是否一致
        if (!newPwd.equals(rePwd)){
            return Result.error("两次填写的密码不一致");
        }
        userService.updatePassword(newPwd);
        //删除redis对应的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success();
    }


    //发送邮件
    @GetMapping("/sendEmail")
    public Result<String> sendEmail(@RequestParam @Email String email){

        User user = userService.selectByEmail(email);
        if (user != null){
            //email = "1729121348@qq.com";
            //生成验证码
            verifyCode = Captcha.genCode();

            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("854234687@qq.com");
            message.setTo(email);
            message.setSubject("big-event 官方邮件 ");
            message.setSentDate(new Date());
            message.setText("你正在修改密码，验证码为:  " + verifyCode);

            javaMailSender.send(message);

            return Result.success(verifyCode);
        }

        return Result.error("不存在该邮箱");



    }

    /*
    * @RequestBody Map<String , String> map
    * @RequestParam String code , @RequestParam String email
    * */

    //找回密码
    @PatchMapping("/findPwd")
    public Result retrievePassword(@RequestBody Map<String , String> map){
        String email = map.get("email");
        String code = map.get("code");
        String newPwd = map.get("new_pwd");
        String rePwd = map.get("re_pwd");

        if (!StringUtils.hasLength(code) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error("缺少必要的参数");
        }

        if (code.equals(verifyCode)){
            User user = userService.selectByEmail(email);
            //newPwd与rePwd是否一致
            if (!newPwd.equals(rePwd)){
                return Result.error("两次填写的密码不一致");
            }
        }

        userService.findPassword(newPwd , email);

        return Result.success();
    }
}
