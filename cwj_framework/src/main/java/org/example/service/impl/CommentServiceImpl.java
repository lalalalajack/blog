package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.constant.SystemConstants;
import org.example.dao.CommentDao;
import org.example.domain.ResponseResult;
import org.example.domain.entity.Comment;
import org.example.domain.vo.CommentVo;
import org.example.domain.vo.PageVo;
import org.example.enums.AppHttpCodeEnum;
import org.example.exception.SystemException;
import org.example.service.CommentService;
import org.example.service.UserService;
import org.example.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-06-05 22:27:39
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    /**
     * 查询评论 （查询根评论/即rootId=-1)
     * @param articleId 文章id
     * @param pageNum   评论分页 当前页
     * @param pageSize  评论分页 页大小
     * @return PageVo(commentVos,iPage.getTotal())
     */
    @Override
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论
        //根据articleId查询对应文章
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,articleId);
        //根评论的rootId为-1
        queryWrapper.eq(Comment::getRootId, SystemConstants.ROOT_COMMENT);
        queryWrapper.orderByDesc(Comment::getCreateTime);

        //分页查询
        //包装分页模型
        Page<Comment> iPage = new Page<>(pageNum, pageSize);
        //执行查询
        page(iPage,queryWrapper);
        List<Comment> records = iPage.getRecords();

        //封装查询结果（先不考虑子评论）
        List<CommentVo> commentVos = toCommentVoList(records);

        //查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVo commentVo : commentVos) {
            //查询对应子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            //赋值
            commentVo.setChildren(children);
        }

        //stream流写法
//        commentVos.stream()
//                .map(commentVo -> commentVo.setChildren(getChildren(commentVo.getId())));


        return ResponseResult.okResult(new PageVo(commentVos,iPage.getTotal()));
    }

    /**
     * 发表评论
     *
     * @param comment 评论内容
     * @return
     */
    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不为空
        //TODO转化为在DTO类中校验
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 将CommentPOJO封装成CommentVo
     * @param list Comment
     * @return CommentVo
     */
    private List<CommentVo> toCommentVoList(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历vo集合
        for (CommentVo commentVo : commentVos) {
            //通过createBy查询用户的昵称并赋值
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            //通过toCommentId查询用户的昵称并赋值
            //如果toCommentId不问不为=1才进行查询
            if(commentVo.getToCommentId()!=-1){
                String nickName1 = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(nickName1);
            }
        }
//        commentVos = commentVos.stream()
//                .peek(commentVo -> {
//                    commentVo.setUsername(userService.getById(commentVo.getCreateBy()).getNickName());
//                    if (commentVo.getToCommentId() != -1) {
//                        commentVo.setToCommentUserName(userService.getById(commentVo.getToCommentId()).getNickName());
//                    }
//                })
//                .collect(Collectors.toList());
        return commentVos;
    }


    /**
     * 根据根评论的id查询所对应的子评论的集合
     * @param id 根评论的id
     * @return 封装好的CommentVo
     */
    private List<CommentVo> getChildren(Long id){
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByDesc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);
        return toCommentVoList(comments);
    }
}

