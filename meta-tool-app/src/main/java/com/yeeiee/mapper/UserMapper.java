package com.yeeiee.mapper;

import com.yeeiee.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * User Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2024/5/7
 */
public interface UserMapper {
    User getUserByName(@Param("username") String username);
}
