package com.blllf.bigevent.service;

import com.blllf.bigevent.pojo.Category;

import java.util.List;

public interface CategoryService {
    void add(Category category);

    List<Category> selectAll();

    List<Category> selectAll2();

    //根据Id查询
    Category selectOneById(Integer id);

    void update(Category category);

    void delete(Integer id);


    List<Category> select5Category();
}
