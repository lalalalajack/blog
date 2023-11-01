package org.example.domain.vo;

import lombok.Data;

/**
 * 热门文章VO
 */
@Data
public class HotArticleVo {
    private Long id;
    //标题
    private String title;
    //访问量
    private Long viewCount;
}
