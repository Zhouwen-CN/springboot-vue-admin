package com.yeeiee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.entity.User;
import com.yeeiee.domain.vo.UserRoleVo;
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
     * 查询用户列表
     *
     * @param page 分页对象
     * @return 用户分页对象
     */
    IPage<UserRoleVo> selectUserPages(Page<UserRoleVo> page, @Param("searchName") String searchName);
}
