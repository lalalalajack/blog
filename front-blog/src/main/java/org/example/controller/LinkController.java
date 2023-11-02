package org.example.controller;

import org.example.domain.ResponseResult;
import org.example.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    /**
     * 友联查询
     * @return 在友链页面要查询出所有的审核通过的友链。
     */
    @GetMapping("/getAllLink")
    public ResponseResult getAllLink(){
        return this.linkService.getAllLink();
    }
}
