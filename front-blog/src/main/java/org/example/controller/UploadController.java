package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.domain.ResponseResult;
import org.example.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "上传",description = "上传图片接口")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    /**
     * 头像上传
     * @param img 图片
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation(value = "上传图片",notes = "")
    public ResponseResult uploadImg(MultipartFile img) {
        return uploadService.uplopadImg(img);
    }
}
