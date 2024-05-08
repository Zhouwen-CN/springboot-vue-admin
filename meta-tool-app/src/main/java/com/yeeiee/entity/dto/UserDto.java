package com.yeeiee.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * <p>
 * 用户 传输对象
 * </p>
 *
 * @author chen
 * @since 2024/5/8
 */
@Getter
@Setter
@ToString
@Schema(name = "User", description = "用户传输对象")
public class UserDto {
    @Schema(description = "主键")
    private Integer id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "权限列表")
    private Set<String> authorities;
}
