package com.yeeiee.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.entity.User;
import com.yeeiee.entity.dto.LoginDto;
import com.yeeiee.entity.dto.UserRoleIdsDto;
import com.yeeiee.entity.vo.UserRoleMenuVo;
import com.yeeiee.entity.vo.UserRoleVo;

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
     * 用户登入
     *
     * @param loginDto 登入传输对象
     * @return token
     */
    String login(LoginDto loginDto);

    /**
     * 获取用户信息，包含菜单和角色信息
     *
     * @return 用户信息
     */
    UserRoleMenuVo getUserInfo();

    /**
     * 获取用户分页，包含用户角色信息
     *
     * @param page       分页配置
     * @param searchName 搜索关键词
     * @return 用户分页对象
     */
    IPage<UserRoleVo> getUserPages(Page<UserRoleVo> page, String searchName);


    /**
     * 新增用户和角色关系
     *
     * @param userRoleIdsDto 用户和角色关系
     */
    void addUserWithRoleIds(UserRoleIdsDto userRoleIdsDto);

    /**
     * 更新用户和角色关系
     *
     * @param userRoleIdsDto 用户和角色关系
     */
    void updateUserWithRoleIds(UserRoleIdsDto userRoleIdsDto);

    /**
     * 删除用户和角色关系
     *
     * @param id 用户id
     */
    void removeUserWithRoleIds(Long id);
}
