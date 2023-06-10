package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.dao.UserDao;
import org.example.domain.ResponseResult;
import org.example.domain.entity.LoginUser;
import org.example.domain.entity.User;
import org.example.domain.entity.UserRole;
import org.example.domain.vo.PageVo;
import org.example.domain.vo.UserInfoVo;
import org.example.domain.vo.UserVo;
import org.example.enums.AppHttpCodeEnum;
import org.example.exception.SystemException;
import org.example.service.UserRoleService;
import org.example.service.UserService;
import org.example.utils.BeanCopyUtils;
import org.example.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-06-05 23:49:46
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;


    /**
     * 个人中心的当前用户信息
     * 需携带token头
     * @return userInfoVo
     */
    @Override
    public ResponseResult userInfo() {
        //获取当用户id
        Long userId = SecurityUtils.getUserId();
        //通过用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    /**
     * 更新个人信息
     * @param user 待更新的个人信息
     * @return code,msg
     */
    @Override
    public ResponseResult updateInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    /**
     * 用户注册
     * @param user 用户信息
     * @return code,msg
     */
    @Override
    public ResponseResult register(User user) {
        //TODO 转为使用DTO
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }

        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult selectUserPage(User user, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.hasText(user.getUserName()),User::getUserName,user.getUserName());
        queryWrapper.eq(StringUtils.hasText(user.getStatus()),User::getStatus,user.getStatus());
        queryWrapper.eq(StringUtils.hasText(user.getPhonenumber()),User::getPhonenumber,user.getPhonenumber());

        Page<User> iPage = new Page<>(pageNum,pageSize);
        page(iPage,queryWrapper);

        //转换成VO
        List<User> users = iPage.getRecords();
        List<UserVo> userVoList = users.stream()
                .map(u -> BeanCopyUtils.copyBean(u, UserVo.class))
                .collect(Collectors.toList());
        PageVo pageVo = new PageVo(userVoList, iPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public boolean checkUserNameUnique(String userName) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getUserName,userName))==0;
    }

    @Override
    public boolean checkPhoneUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getPhonenumber,user.getPhonenumber()))==0;
    }

    @Override
    public boolean checkEmailUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getEmail,user.getEmail()))==0;
    }

    @Override
    public ResponseResult addUser(User user) {
        //TODO 通过DTO 进行校验
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if (!checkUserNameUnique(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (!checkPhoneUnique(user)) {
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if (!checkEmailUnique(user)) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        //密码加密处理
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);

        //同时更新U-R映射
        if(user.getRoleIds()!=null&&user.getRoleIds().length>0){
            insertUserRole(user);
        }
        return ResponseResult.okResult();
    }

    @Override
    public void updateUser(User user) {
        //更新用户，需要先删除U-R映射
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(queryWrapper);

        //再增加U-R映射
        insertUserRole(user);

        //更新用户信息
        updateById(user);
    }

    private void insertUserRole(User user) {
        List<UserRole> userRoleList = Arrays.stream(user.getRoleIds())
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
    }

    /**
     * 从数据库中查询 nickName是否存在
     * @param nickName 昵称
     * @return 是否存在
     */
    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,nickName);
        return count(queryWrapper)>0;
    }

    /**
     * 从数据库中查询 userName是否存在
     * @param userName 用户名
     * @return 是否存在
     */
    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        return count(queryWrapper)>0;
    }
}

