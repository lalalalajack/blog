package org.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserInfoVo {

    //权限集合
    private List<String> permissions;

    //角色集合
    private List<String> roles;

    //用户信息
    private UserInfoVo user;
}