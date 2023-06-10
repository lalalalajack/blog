package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddArticleDto;
import org.example.domain.dto.ArticleDto;
import org.example.domain.entity.Article;
import org.example.domain.vo.ArticleVo;
import org.example.domain.vo.PageVo;
import org.example.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
@Api(tags = "文章", description = "文章相关接口")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 新增博文接口
     *
     * @param article
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增博文接口", notes = "")
    public ResponseResult add(@RequestBody AddArticleDto article) {
        return articleService.add(article);
    }


    /**
     * 查询文章列表
     *
     * @param article  只取用 title 文章标题 + summary 文章摘要
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Article article, Integer pageNum, Integer pageSize) {
        //TODO 将 article 转为 用 dto
        PageVo pageVo = articleService.selectArticlePage(article, pageNum, pageSize);
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 查询文章详情接口
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id") Long id) {
        ArticleVo article = articleService.getInfo(id);
        return ResponseResult.okResult(article);
    }

    /**
     * 修改文章接口
     * @param article
     * @return
     */
    @PutMapping
    public ResponseResult edit(@RequestBody ArticleDto article) {
        articleService.edit(article);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id) {
        articleService.removeById(id);
        return ResponseResult.okResult();
    }



}
