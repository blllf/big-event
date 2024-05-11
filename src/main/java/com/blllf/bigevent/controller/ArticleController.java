package com.blllf.bigevent.controller;


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


}
