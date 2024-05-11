package com.blllf.bigevent.service;

import com.blllf.bigevent.pojo.Article;
import com.blllf.bigevent.pojo.ArticleILike;
import com.blllf.bigevent.pojo.PageBean;

import java.util.List;

public interface ArticleService {
    void add(Article article);

    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);

    //查询所有
    PageBean<Article> selectAll(Integer pageNum, Integer pageSize, Integer categoryId, String title);

    Article selectArtById(Integer id);

    void update(Article article);

    void deleteById(Integer id);

    //添加文章到我的收藏
    void addArticle(Integer articleId);
    List<Article> selectArticlesByUserId();
}
