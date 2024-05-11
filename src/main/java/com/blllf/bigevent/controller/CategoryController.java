package com.blllf.bigevent.controller;


import com.blllf.bigevent.pojo.Category;
import com.blllf.bigevent.pojo.Result;
import com.blllf.bigevent.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public Result add(@RequestBody @Validated Category category){
        categoryService.add(category);
        return Result.success();
    }

    //查询所有分类
    @GetMapping
    public Result<List<Category>> selectAll(){
        List<Category> list = categoryService.selectAll();
        return Result.success(list);
    }

    //查询所有用户的所有分类
    @GetMapping("/all")
    public Result<List<Category>> selectAll2(){
        List<Category> list = categoryService.selectAll2();
        return Result.success(list);
    }

    @GetMapping("/detail")
    public Result<Category> selectById(@RequestParam Integer id){
        Category c = categoryService.selectOneById(id);
        return Result.success(c);
    }



    @PutMapping
    public Result update(@RequestBody @Validated Category category){
        categoryService.update(category);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(Integer id){
        categoryService.delete(id);
        return Result.success();
    }

}
