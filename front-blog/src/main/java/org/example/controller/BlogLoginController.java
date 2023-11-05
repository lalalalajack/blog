package org.example.controller;

import org.example.domain.ResponseResult;
import org.example.domain.dto.UserDto;
import org.example.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody UserDto userDto){
        return blogLoginService.login(userDto);
    }
}
