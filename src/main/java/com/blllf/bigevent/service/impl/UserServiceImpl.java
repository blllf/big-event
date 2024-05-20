package com.blllf.bigevent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blllf.bigevent.mapper.ArticleMapper;
import com.blllf.bigevent.mapper.LikeArticlesMapper;
import com.blllf.bigevent.mapper.UserMapper;
import com.blllf.bigevent.pojo.Article;
import com.blllf.bigevent.pojo.ArticleILike;
import com.blllf.bigevent.pojo.PageBean;
import com.blllf.bigevent.pojo.User;
import com.blllf.bigevent.service.UserService;
import com.blllf.bigevent.util.Md5Util;
import com.blllf.bigevent.util.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    public ArticleMapper articleMapper;
    @Autowired
    public LikeArticlesMapper likeArticlesMapper;

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
        /*//对密码加密
        String md5String = Md5Util.getMD5String(password);

        userMapper.add(username,md5String);*/

        /*暂时不进行加密 开发阶段*/
        userMapper.add(username,password);
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

    //管理员
    // 删除用户需要删除与用户有关的数据
    @Override
    public boolean deleteUser(Integer id) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("create_user", id);

        QueryWrapper<ArticleILike> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("create_user" , id);

        int delete = articleMapper.delete(wrapper);
        int delete1 = likeArticlesMapper.delete(wrapper1);
        int delete2 = userMapper.deleteById(id);

        return delete > 0 || delete1 > 0 || delete2 > 0;
    }

    @Override
    public Boolean updateUserByAdmin( User user) {

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email" , user.getEmail())
                .eq("nickname" , user.getNickname())
                .eq("password" , user.getPassword());
        List<User> user1 = userMapper.selectList(wrapper);

        //说明该用户的邮箱 ， 密码 ， 昵称 已存在
        if (user1 == null){
            return false;
        }

        System.out.println("执行了码");
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateUserAdmin(user);
        return true;
    }

    @Override
    public PageBean<User> selectAllByAdmin(Integer pageNum, Integer pageSize, String username, String email , String nickName) {
        PageBean<User> pb = new PageBean<>();
        PageHelper.startPage(pageNum,pageSize);
        if (username != null && !username.isEmpty()){
            username = "%" + username + "%";
        }
        if (email != null && !email.isEmpty()){
            email = "%" + email + "%";
        }
        if (nickName != null && !nickName.isEmpty()){
            nickName = "%" + nickName + "%";
        }

        Page<User> users = userMapper.findAllUsersByAdmin(username , email , nickName);
        pb.setTotal(users.getTotal());
        pb.setItems(users.getResult());
        return pb;
    }


}
