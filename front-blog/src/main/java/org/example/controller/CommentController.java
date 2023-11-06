package org.example.controller;

import org.example.constant.SystemConstants;
import org.example.domain.ResponseResult;
import org.example.domain.entity.Comment;
import org.example.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
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
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize){
        return commentService.commentList( SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }



    /**
     * 发表评论
     * @param comment 评论内容
     * @return
     */
    @PostMapping
    public ResponseResult addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }


}
