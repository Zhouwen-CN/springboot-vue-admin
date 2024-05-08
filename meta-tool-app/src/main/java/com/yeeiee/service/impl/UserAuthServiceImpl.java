package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.entity.UserAuth;
import com.yeeiee.mapper.UserAuthMapper;
import com.yeeiee.service.UserAuthService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户权限关系表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2024-05-08
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements UserAuthService {

}
