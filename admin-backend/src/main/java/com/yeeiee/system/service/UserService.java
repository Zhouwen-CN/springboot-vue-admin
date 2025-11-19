package com.yeeiee.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.system.domain.entity.User;
import com.yeeiee.system.domain.form.ChangePwdForm;
import com.yeeiee.system.domain.form.UserForm;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Collection;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
public interface UserService extends IService<User> {
    /**
     * 登出
     */
    void logout();

    /**
     * 新增用户和角色关系
     *
     * @param userForm 用户和角色关系
     */
    void addUser(UserForm userForm);

    /**
     * 更新用户和角色关系
     *
     * @param userForm 用户和角色关系
     */
    void modifyUser(UserForm userForm);

    /**
     * 删除用户和角色关系
     *
     * @param id 用户id
     */
    void removeUserById(Long id);

    /**
     * 批量删除用户和角色关系
     *
     * @param ids 用户id列表
     */
    void removeUserByIds(Collection<Long> ids);

    /**
     * 修改密码
     *
     * @param changePwdForm 修改密码表单
     */
    void modifyUserChangePwd(ChangePwdForm changePwdForm);

    /**
     * 重置密码
     *
     * @param id 用户id
     */
    void modifyUserResetPwd(Long id);

    /**
     * 根据用户ID查询用户
     * @param userId 用户id
     * @return 用户
     */
    User getUserByUserId(Long userId);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    User getUserByUsername(String username);

    /**
     * 修改用户 refreshToken
     *
     * @param userId       用户
     * @param refreshToken 刷新token
     */
    void modifyRefreshTokenByUserId(Long userId, String refreshToken);

    /**
     * 刷新token
     *
     * @param token    access token
     * @param response response
     */
    void refreshToken(String token, HttpServletResponse response);
}
