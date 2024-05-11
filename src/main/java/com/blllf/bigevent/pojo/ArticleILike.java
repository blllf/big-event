package com.blllf.bigevent.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArticleILike {

    private Integer id;
    private Integer userId;
    private LocalDateTime collectionTime;
    private Integer articleId;

    private List<Article> articleList;      //用户关联的文章

}
