package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.example.constant.SystemConstants;
import org.example.domain.ResponseResult;
import org.example.domain.dto.AddCommentDto;
import org.example.domain.entity.Comment;
import org.example.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论",description = "评论相关接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 查询文章评论
     * @param articleId 文章id
     * @param pageNum   评论分页 当前页
     * @param pageSize  评论分页 页大小
     * @return
     */
    @GetMapping("/commentList")
    @ApiOperation(value = "查询文章评论",notes = "获取一页文章评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value = "文章号"),
            @ApiImplicitParam(name = "pageNum", value = "页号"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小")
    })
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }

    /**
     * 查询友链评论
     * @param pageNum   评论分页 当前页
     * @param pageSize  评论分页 页大小
     * @return
     */
    @GetMapping("/linkCommentList")
    @ApiOperation(value = "友链评论列表",notes = "获取一页友链评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "页号"),
            @ApiImplicitParam(name = "pageSize",value = "每页大小")
    })
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize){
        return commentService.commentList( SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }



    /**
     * 发表评论
     * @param
     * @return
     */
    @PostMapping
    @ApiOperation(value = "发表评论",notes = "发表")
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto){
        return commentService.addComment(addCommentDto);
    }


}
