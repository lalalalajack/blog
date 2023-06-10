package org.example.controller;


import org.example.domain.ResponseResult;
import org.example.domain.dto.ChangeRoleStatusDto;
import org.example.domain.dto.ChangeUserStatusDto;
import org.example.domain.entity.Role;
import org.example.domain.entity.User;
import org.example.domain.vo.UserInfoAndRoleIdsVo;
import org.example.enums.AppHttpCodeEnum;
import org.example.exception.SystemException;
import org.example.service.RoleService;
import org.example.service.UserService;
import org.example.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    public ResponseResult list(User user, Integer pageNum, Integer pageSize) {
        // TODO user 可以转为 dto对象
        return userService.selectUserPage(user, pageNum, pageSize);
    }

    /**
     * 新增用户
     */
    @PostMapping
    public ResponseResult add(@RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * 根据用户编号获取详细信息
     */
    @GetMapping(value = {"/{userId}"})
    public ResponseResult getUserInfoAndRoleIds(@PathVariable(value = "userId") Long userId) {
        List<Role> roles = roleService.selectRoleAll();
        User user = userService.getById(userId);
        //当前用户所具有的角色id列表
        List<Long> roleIds = roleService.selectRoleIdByUserId(userId);

        UserInfoAndRoleIdsVo vo = new UserInfoAndRoleIdsVo(user, roles, roleIds);
        return ResponseResult.okResult(vo);
    }

    /**
     * 修改用户
     */
    @PutMapping
    public ResponseResult edit(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseResult.okResult();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userIds}")
    public ResponseResult remove(@PathVariable List<Long> userIds) {
        if (userIds.contains(SecurityUtils.getUserId())) {
            return ResponseResult.errorResult(500, "不能删除当前你正在使用的用户");
        }
        userService.removeByIds(userIds);
        return ResponseResult.okResult();
    }

    /**
     * 改变用户状态接口
     * @param userStatusDto
     * @return
     */
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeUserStatusDto userStatusDto) {
        User user = new User();
        user.setId(userStatusDto.getUserId());
        user.setStatus(userStatusDto.getStatus());
        return ResponseResult.okResult(userService.updateById(user));
    }
}
