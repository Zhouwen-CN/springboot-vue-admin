package com.yeeiee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.entity.User;
import com.yeeiee.entity.vo.UserInfoVo;
import com.yeeiee.entity.vo.UserRoleVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据用户名称查询用户信息
     *
     * @param username 用户名称
     * @return 用户对象
     */
    UserInfoVo selectByUserName(@Param("username") String username);

    /**
     * 查询用户列表
     *
     * @param page 分页对象
     * @return 用户分页对象
     */
    IPage<UserRoleVo> selectUserPages(Page<UserRoleVo> page, @Param("searchName") String searchName);
}
