package com.yeeiee.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 用户登入 传输对象
 * </p>
 *
 * @author chen
 * @since 2024/5/7
 */
@Getter
@Setter
@ToString
public class LoginDto {
    private String username;
    private String password;
}
