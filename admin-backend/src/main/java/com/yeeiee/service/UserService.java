package com.yeeiee.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.domain.entity.User;
import com.yeeiee.domain.form.ChangePwdForm;
import com.yeeiee.domain.form.UserForm;
import com.yeeiee.domain.vo.UserVo;

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
    IPage<UserVo> getUserPages(Page<UserVo> page, String searchName);


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
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    User getUserByUsername(String username);

    /**
     * 修改用户token版本
     * @param user 用户
     */
    void modifyTokenVersionByUser(User user);
}
