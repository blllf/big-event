package com.blllf.bigevent.service.impl;

import com.blllf.bigevent.mapper.CategoryMapper;
import com.blllf.bigevent.pojo.Category;
import com.blllf.bigevent.service.CategoryService;
import com.blllf.bigevent.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void add(Category category) {
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());

        Map<String , Object> map = ThreadLocalUtil.get();
        Integer uId = (Integer) map.get("id");
        category.setCreateUser(uId);

        categoryMapper.add(category);
    }

    @Override
    public List<Category> selectAll() {
        Map<String , Object> map = ThreadLocalUtil.get();
        Integer uId = (Integer) map.get("id");
        return categoryMapper.selectAll(uId);
    }

    @Override
    public List<Category> selectAll2() {
        return categoryMapper.selectAll2();
    }

    @Override
    public Category selectOneById(Integer id) {
        Category c = categoryMapper.selectOneById(id);
        return c;
    }

    @Override
    public void update(Category category) {
        categoryMapper.update(category);
    }

    @Override
    public void delete(Integer id) {
        categoryMapper.delete(id);
    }
}
