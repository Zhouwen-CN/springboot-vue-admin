package com.yeeiee.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeeiee.domain.vo.UserVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2025-09-05
 */
public interface UserMapper extends BaseMapper<User> {
    IPage<UserVo> selectUserPages(Page<UserVo> page, @Param("searchName") String searchName);
}
