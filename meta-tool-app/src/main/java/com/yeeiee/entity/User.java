package com.yeeiee.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author chen
 * @since 2024/5/7
 */
@Getter
@Setter
@ToString
@Schema(name = "User", description = "用户表")
public class User {
    @Schema(description = "主键")
    private Integer id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "权限列表")
    private Set<String> authorities;
}
