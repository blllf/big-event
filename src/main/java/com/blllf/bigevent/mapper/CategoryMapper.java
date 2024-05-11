package com.blllf.bigevent.mapper;

import com.blllf.bigevent.pojo.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    //新增分类
    @Insert("insert into category(category_name, category_alias, create_user, create_time, update_time) VALUES " +
            "(#{categoryName} , #{categoryAlias} ,#{createUser} ,#{createTime} , #{updateTime})")
    void add(Category category);


    @Select("select * from category where create_user = #{uId}")
    List<Category> selectAll(Integer uId);

    //查询所有用户的分类信息
    @Select("select * from category")
    List<Category> selectAll2();

    @Select("select * from category where id = #{id}")
    Category selectOneById(Integer id);

    @Update("update category set category_name = #{categoryName} ,category_alias = #{categoryAlias} , update_time = now()" +
            "where id = #{id}")
    void update(Category category);

    @Delete("delete from category where id = #{id}")
    void delete(Integer id);
}
