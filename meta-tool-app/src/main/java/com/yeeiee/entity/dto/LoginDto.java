package com.yeeiee.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "LoginDto", description = "用户登入传输对象")
public class LoginDto {
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    private String password;
}
