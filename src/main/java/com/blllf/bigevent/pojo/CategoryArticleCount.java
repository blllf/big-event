package com.blllf.bigevent.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryArticleCount {

    private Integer id;
    private Integer categoryId;
    private Integer totalCount;
    private String categoryName;
    private String categoryAlias;

}
