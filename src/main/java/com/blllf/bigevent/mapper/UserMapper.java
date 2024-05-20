package com.blllf.bigevent.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blllf.bigevent.pojo.User;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user")
    List<User> selectAll();
    @Select("select * from user where username = #{username}")
    User findByUsername(String username);

    @Insert("insert into user(username, password, create_time, update_time) VALUES " +
            "(#{username} , #{password} , now() , now())")
    void add(String username , String password);


    @Update("update user set nickname = #{nickname} , email = #{email} , update_time = #{updateTime} where id = #{id}")
    void update(User user);

    //更新头像
    @Update("update user set user_pic = #{avatarUrl} , update_time = now() where id = #{id}")
    void updateAvatar(String avatarUrl , Integer id);

    //更新密码
    @Update("update user set password = #{password} , update_time = now() where id = #{id}")
    void updatePassword(String password , Integer id);

    //根据用户邮箱查询用户
    @Select("select * from user where email = #{email}")
    User selectByEmail(String email);

    @Select("select * from user where email = #{email} and nickname = #{nickname}")
    User selectByEmail2(String email , String nickname );

    @Update("update user set password = #{password} , update_time = now() where email = #{email}")
    void findPasswordByEmail(String password , String email);

    //管理员操作修改
    @Update("update user set nickname = #{nickname} , email = #{email} , update_time = #{updateTime} , password = #{password} where id = #{id}")
    void updateUserAdmin(User user);

    @Insert("insert into user(username , nickname, email , password, create_time, update_time) VALUES " +
            "(#{username} ,#{nickname} , #{email} , #{password} , now() , now())")
    void addUserAdmin(User user);


    Page<User> findAllUsersByAdmin(String username, String email, String nickname);
}
