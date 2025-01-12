package com.yeeiee.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.entity.User;
import com.yeeiee.entity.dto.UserRoleIdsDto;
import com.yeeiee.entity.vo.TokenVo;
import com.yeeiee.entity.vo.UserRoleVo;
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
    TokenVo refreshToken(HttpServletRequest request);

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
     * @param userRoleIdsDto 用户和角色关系
     */
    void addUser(UserRoleIdsDto userRoleIdsDto);

    /**
     * 更新用户和角色关系
     *
     * @param userRoleIdsDto 用户和角色关系
     */
    void modifyUser(UserRoleIdsDto userRoleIdsDto);

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
}
