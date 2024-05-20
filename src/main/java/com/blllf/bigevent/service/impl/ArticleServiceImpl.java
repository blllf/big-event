package com.blllf.bigevent.service.impl;

import com.blllf.bigevent.mapper.ArticleMapper;
import com.blllf.bigevent.pojo.Article;
import com.blllf.bigevent.pojo.ArticleILike;
import com.blllf.bigevent.pojo.PageBean;
import com.blllf.bigevent.service.ArticleService;
import com.blllf.bigevent.util.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    //新增文章
    @Override
    public void add(Article article) {
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());

        Map<String,Object> map = ThreadLocalUtil.get();
        Integer uid = (Integer) map.get("id");
        article.setCreateUser(uid);
        articleMapper.add(article);
    }
    /**
     * 使用PageHelper 插件进行分页
     * */
    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        PageBean<Article> pb = new PageBean<>();

        PageHelper.startPage(pageNum,pageSize);

        Map<String,Object> map = ThreadLocalUtil.get();
        Integer uid = (Integer) map.get("id");
        Page<Article> articles = articleMapper.list(uid,categoryId, state);

        pb.setTotal(articles.getTotal());
        pb.setItems(articles.getResult());

        return pb;
    }

    @Override
    public PageBean<Article> selectAll(Integer pageNum, Integer pageSize, Integer categoryId, String title) {
        PageBean<Article> pb = new PageBean<>();
        PageHelper.startPage(pageNum,pageSize);
        //文章状态只能是已发布
        String state = "已发布";
        if (title != null && !title.isEmpty()){
            title = "%" + title + "%";
           // System.out.println(title);
        }
        Page<Article> articles = articleMapper.selectAll(categoryId, state, title);

        pb.setTotal(articles.getTotal());
        pb.setItems(articles.getResult());
        return pb;
    }

    @Override
    public Article selectArtById(Integer id) {
        Article article = articleMapper.selectArtById(id);
        return article;
    }

    @Override
    public void update(Article article) {

        articleMapper.update(article);
    }

    @Override
    public void deleteById(Integer id) {
        articleMapper.deleteById("create_user", id);

        //还要删除掉收藏中的数据
        articleMapper.deleteArticleById(id);
    }


    //添加文章到我的收藏
    @Override
    public void addArticle(Integer articleId) {
        ArticleILike articleILike = new ArticleILike();

        articleILike.setArticleId(articleId);
        articleILike.setCollectionTime(LocalDateTime.now());
        Map<String , Object> map = ThreadLocalUtil.get();
        Integer uid = (Integer) map.get("id");

        articleILike.setUserId(uid);

        articleMapper.addArticleToLike(articleILike);
    }

    @Override
    public List<Article> selectArticlesByUserId() {

        Map<String,Object> map = ThreadLocalUtil.get();
        Integer uid = (Integer) map.get("id");
        List<Article> articles = articleMapper.selectArticleByUserId(uid);

        return articles;
    }


}
