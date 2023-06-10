package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.ResponseResult;
import org.example.domain.dto.TagListDto;
import org.example.domain.entity.Tag;
import org.example.domain.vo.PageVo;
import org.example.domain.vo.TagVo;

import java.util.List;

/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-06-09 11:09:15
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageSize, Integer pageSize1, TagListDto tagListDto);


    List<TagVo> listAllTag();
}

