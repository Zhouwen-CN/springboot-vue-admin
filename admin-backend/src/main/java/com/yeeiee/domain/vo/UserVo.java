package com.yeeiee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <p>
 * 角色视图对象
 * </p>
 *
 * @author chen
 * @since 2025-11-12
 */
@Getter
@Setter
@ToString
@Schema(name = "UserVo", description = "用户视图对象")
public class UserVo {
    @Schema(description = "用户id")
    private Long id;
    @Schema(description = "用户名称")
    private String username;
    @Schema(description = "访问 token")
    private String accessToken;
    @Schema(description = "刷新 token")
    private String refreshToken;
    @Schema(description = "用户角色列表")
    private List<String> roles;
}
