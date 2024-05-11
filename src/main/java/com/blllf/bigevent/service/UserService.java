package com.blllf.bigevent.service;

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
}
