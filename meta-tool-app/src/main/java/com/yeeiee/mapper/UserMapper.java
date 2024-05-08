package com.yeeiee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeeiee.entity.User;
import com.yeeiee.entity.dto.UserDto;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2024-05-08
 */
public interface UserMapper extends BaseMapper<User> {
    UserDto getUserByName(@Param("username") String username);
}
