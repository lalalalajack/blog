package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddArticleDto;
import org.example.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content/article")
@Api(tags = "文章",description = "文章相关接口")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 新增博文接口
     * @param article
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增博文接口",notes = "")
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }
}
