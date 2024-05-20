package com.blllf.bigevent.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blllf.bigevent.mapper.ArticleMapper;
import com.blllf.bigevent.pojo.*;
import com.blllf.bigevent.service.ArticleService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleMapper articleMapper;

    @PostMapping
    public Result add(@RequestBody @Validated Article article){
        articleService.add(article);
        return Result.success();
    }


    //分页
    @GetMapping
    public Result<PageBean<Article>> list(Integer pageNum , Integer pageSize ,
                                          @RequestParam(required = false) Integer categoryId ,
                                          @RequestParam(required = false) String state){

        PageBean<Article> list =articleService.list(pageNum , pageSize , categoryId , state);

        return Result.success(list);
    }

    //查询文章广场
    @GetMapping("/selectAll")
    public Result<PageBean<Article>> selectAll(Integer pageNum , Integer pageSize ,
                                               @RequestParam(required = false) Integer categoryId ,
                                               @RequestParam(required = false) String title){
        PageBean<Article> articles = articleService.selectAll(pageNum, pageSize, categoryId, title);

        return Result.success(articles);
    }


    //获取文章基本详情
    @GetMapping("/detail")
    public Result<Article> selectArtById(@Validated @NotNull Integer id){
        Article article = articleService.selectArtById(id);
        return Result.success(article);
    }

    //更新文章
    @PutMapping
    public Result update(@RequestBody @Validated Article article){
        articleService.update(article);
        return Result.success();
    }

    @DeleteMapping
    public Result delet(@Validated @NotNull Integer id){
        articleService.deleteById(id);
        return Result.success();
    }

    //查询不同分类文章的数量
    @GetMapping("/selectArticleByCategory")
    public Result<List<CategoryArticleCount>> selectArticleByCategory(){

        List<CategoryArticleCount> cas = articleMapper.selectArticleByCategory();

        return Result.success(cas);

    }

    //文章收藏
    @GetMapping("/addArticleILike")
    public Result addArticleILike(@NotNull Integer articleId){
        articleService.addArticle(articleId);
        return Result.success();
    }
    @PostMapping("/selectForCollection")
    public Result<List<Article>> selectForCollection(){

        List<Article> articles = articleService.selectArticlesByUserId();

        return Result.success(articles);
    }

    @GetMapping("/deleteArticleById")
    public Result deleteArticleById(@NotNull Integer articleId ){
        articleMapper.deleteArticleById(articleId);
        return Result.success();
    }

    /*
    *
    * 采用mybatis-plus 写法
    *
    * */
    @GetMapping("/showArticle")
    public Result<List<Article>> showArticle(){
        QueryWrapper<Article> querywrapper = new QueryWrapper<>();
        querywrapper.eq("state","已发布").orderByDesc("create_time");
        List<Article> articles  = articleMapper.selectList(querywrapper);
        return Result.success(articles);
    }

    @GetMapping("/showPageArticle")
    public Result<PageBean<Article>> showPageArticle(Integer pageNum , Integer pageSize ,
                                                     @RequestParam(required = false) Integer categoryId,
                                                     @RequestParam(required = false) String title){
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("state","已发布");
        if (title != null && title != ""){
            wrapper.like("title" , title);
        }
        if (categoryId != null){
            wrapper.eq("category_id",categoryId);
        }
        Page<Article> page = new Page<>(pageNum, pageSize);
        articleMapper.selectPage(page, wrapper);

        page.getRecords().forEach(System.out::println);
        System.out.println(page.getTotal());
        //把查询到的数据封装到PageBean中
        PageBean<Article> pb = new PageBean<>();
        pb.setTotal(page.getTotal());
        pb.setItems(page.getRecords());

        return Result.success(pb);
    }

    @GetMapping("/id")
    public Result<Article> selectArticleById(Integer id){
        Article article = articleMapper.selectById(id);
        return Result.success(article);
    }

    //根据分类的id查找文章
    @GetMapping("/categoryId")
    public Result<List<Article>> selectArticleByCategoryId(Integer categoryId){
        //使用条件构造器Wrapper
        QueryWrapper<Article> querywrapper = new QueryWrapper<>();
        querywrapper.eq("category_id" , categoryId).eq("state","已发布").orderByDesc("create_time");
        List<Article> articles = articleMapper.selectList(querywrapper);
        return Result.success(articles);
    }

    @GetMapping("/findArticlesByPage")
    public Result<List<Article>> findArticlesByPage(Integer categoryId,
                                                @RequestParam(required = false) String username ,
                                                @RequestParam(required = false) String title){
        if (username != null & username != ""){
            username = "%" + username + "%";
        }
        if (title != null & title != ""){
            title = "%" + title + "%";
        }
        List<Article> articles = articleMapper.findArticlesByPage(categoryId , username, title);
        return Result.success(articles);
    }


}
