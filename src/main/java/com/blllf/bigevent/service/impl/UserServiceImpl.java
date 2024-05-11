package com.blllf.bigevent.service.impl;

import com.blllf.bigevent.mapper.UserMapper;
import com.blllf.bigevent.pojo.User;
import com.blllf.bigevent.service.UserService;
import com.blllf.bigevent.util.Md5Util;
import com.blllf.bigevent.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    @Override
    public User findByUsername(String username) {
        User u = userMapper.findByUsername(username);
        return u;
    }

    @Override
    public void register(String username, String password) {
        //对密码加密
        String md5String = Md5Util.getMD5String(password);

        userMapper.add(username,md5String);

    }

    @Override
    public Boolean update(User user) {

        User user1 = userMapper.selectByEmail2(user.getEmail() , user.getNickname());

        //说明邮箱已存在
        if (user1 != null){
            return false;
        }

        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);

        return true;
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String , Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        userMapper.updateAvatar(avatarUrl , id);
    }

    @Override
    public void updatePassword(String password) {
        Map<String , Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        //对密码进行加密
        userMapper.updatePassword(Md5Util.getMD5String(password) , id);
    }

    @Override
    public User selectByEmail(String email) {
        User user = userMapper.selectByEmail(email);
        return user;
    }

    @Override
    public void findPassword(String password , String email) {

        userMapper.findPasswordByEmail(Md5Util.getMD5String(password),email);
    }
}
