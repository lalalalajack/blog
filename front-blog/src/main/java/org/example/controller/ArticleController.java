package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.example.domain.ResponseResult;
import org.example.domain.entity.User;
import org.example.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/article")
@Api(tags = "文章",description = "文章相关接口")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 查询热门文章
     * @return
     */
    @GetMapping("/hotArticleList")
    @ApiOperation(value = "查询热门文章",notes = "获取热门文章，最多10条，按浏览量")
    public ResponseResult hotArticleList(){
        return articleService.hotArticleList();
    }

    /**
     * 分页查询文章列表
     * @return
     */
    @GetMapping("/articleList")
    @ApiOperation(value = "分页查询文章列表",notes = "获取一页文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "当前页"),
            @ApiImplicitParam(name = "pageSize",value = "页大小"),
            @ApiImplicitParam(name = "categoryId",value = "类别id")
    })
    public ResponseResult articleList( Integer pageNum,  Integer pageSize, Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    /**
     * 获取文章详情接口
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取文章详情",notes = "")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }

    /**
     * 更新浏览量时更新redis数据
     * @param id 更新的文章id
     * @return
     */
    @PutMapping("/updateViewCount/{id}")
    @ApiOperation(value = "更新浏览量时更新redis数据",notes = "只更新redis，数据库通过定时任务来更新，以保证性能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "文章id")
    })
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}
