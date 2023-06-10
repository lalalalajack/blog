package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.ResponseResult;
import org.example.domain.entity.Link;
import org.example.domain.vo.PageVo;

/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-06-02 16:26:11
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    PageVo selectLinkPage(Link link, Integer pageNum, Integer pageSize);
}

