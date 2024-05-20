package com.blllf.bigevent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blllf.bigevent.pojo.ArticleILike;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikeArticlesMapper extends BaseMapper<ArticleILike> {
}
