package com.yeeiee.service;

import com.yeeiee.entity.LoginDto;
import com.yeeiee.entity.User;

/**
 * <p>
 * 用户 服务接口
 * </p>
 *
 * @author chen
 * @since 2024/5/7
 */
public interface UserService {
    String login(LoginDto loginDto);

    User getUser();

}
