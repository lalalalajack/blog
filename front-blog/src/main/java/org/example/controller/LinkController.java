package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.domain.ResponseResult;
import org.example.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
@Api(tags = "友链",description = "友链相关接口")
public class LinkController {

    @Autowired
    private LinkService linkService;

    /**
     * 友联查询
     * @return 在友链页面要查询出所有的审核通过的友链。
     */
    @GetMapping("/getAllLink")
    @ApiOperation(value = "查询友链",notes = "")
    public ResponseResult getAllLink(){
        return this.linkService.getAllLink();
    }
}
