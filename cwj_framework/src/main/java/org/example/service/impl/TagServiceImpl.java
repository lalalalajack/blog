package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.dao.TagDao;
import org.example.domain.entity.Tag;
import org.example.service.TagService;
import org.springframework.stereotype.Service;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-06-09 11:09:15
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagDao, Tag> implements TagService {

}

