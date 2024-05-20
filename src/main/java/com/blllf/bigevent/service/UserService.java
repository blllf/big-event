package com.blllf.bigevent.service;

import com.blllf.bigevent.pojo.PageBean;
import com.blllf.bigevent.pojo.User;

import java.util.List;

public interface UserService {
    public List<User> selectAll();
    public User findByUsername(String username);

    public void register(String username , String password);

    public Boolean update(User user);

    public void updateAvatar(String avatarUrl);

    public void updatePassword(String password);

    public User selectByEmail(String email);

    public void findPassword(String password, String email);

    public boolean deleteUser(Integer id);

    //管理员修改用户信息
    public Boolean updateUserByAdmin(User user);

    public PageBean<User> selectAllByAdmin(Integer pageNum , Integer pageSize , String username , String email ,String nickName);
}
