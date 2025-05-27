package com.yeeiee.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.domain.entity.User;
import com.yeeiee.domain.form.ChangePasswordForm;
import com.yeeiee.domain.form.UserForm;
import com.yeeiee.domain.vo.UserRoleVo;
import com.yeeiee.domain.vo.UserVo;
import jakarta.servlet.http.HttpServletRequest;

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
     * 刷新 token
     *
     * @param request 请求对象
     * @return user 对象信息
     */
    UserVo refreshToken(HttpServletRequest request);

    /**
     * 登出
     *
     * @param id 用户 id
     */
    void logout(Long id);

    /**
     * 获取用户分页（包含角色ids）
     *
     * @param page       分页配置对象
     * @param searchName 搜索关键词
     * @return 分页结果
     */
    IPage<UserRoleVo> getUserPages(Page<UserRoleVo> page, String searchName);


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
     * @param changePasswordForm 修改密码表单
     */
    void modifyUserChangePwd(ChangePasswordForm changePasswordForm);

    /**
     * 重置密码
     *
     * @param id 用户id
     */
    void modifyUserResetPwd(Long id);
}
