package com.blllf.bigevent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blllf.bigevent.pojo.Article;
import com.blllf.bigevent.pojo.ArticleILike;
import com.blllf.bigevent.pojo.CategoryArticleCount;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    @Insert("insert into article(title, content, cover_img, state, category_id, create_user, create_time, update_time) VALUES " +
            "(#{title} , #{content} ,#{coverImg} ,#{state}, #{categoryId} , #{createUser} ,#{createTime} ,#{updateTime})")
    void add(Article article);

    Page<Article> list(Integer uid, Integer categoryId, String state);

    //查找所有的文章 根据categoryId state title
    Page<Article> selectAll(Integer categoryId, String state , String title);


    @Select("select * from article where id = #{id}")
    Article selectArtById(Integer id);


    @Update("update article set title = #{title} , content = #{content} , cover_img = #{coverImg} ,state = #{state} , category_id = #{categoryId} , update_time = now() " +
            "where id = #{id}")
    void update(Article article);

    @Delete("delete from article where id = #{id}")
    void deleteById(String createUser, Integer id);
    //必须删除掉收藏中的数据
    @Delete("delete from articlecollection where article_id = #{articleId}")
    void deleteArticleById(Integer articleId);

    //查询不同分类文章的数量
    @Select("select category.id, category_id , count(*) as total_count , category_name , category_alias from category , article " +
            "where category.id = category_id and article.state = '已发布'  GROUP BY article.category_id ;")

    List<CategoryArticleCount> selectArticleByCategory();

    /*
    * 收藏文章
    * */
    @Insert("insert into articlecollection(create_user, collection_time, article_id) values (#{userId} , now() , #{articleId})")
    void addArticleToLike(ArticleILike articleILike);



    @Select("select article.* , articlecollection.collection_time from article , articlecollection  where articlecollection.create_user = #{userId} and  articlecollection.article_id = article.id;")
    List<Article> selectArticleByUserId(Integer userId);

    //分页查询 分类页面
    //Page<Article> findArticlesByPage(Integer page , p)
    List<Article> findArticlesByPage(Integer categoryId ,String username , String title);





}
