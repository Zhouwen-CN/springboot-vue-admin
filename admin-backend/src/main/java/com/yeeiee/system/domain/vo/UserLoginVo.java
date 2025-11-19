package com.yeeiee.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 用户登入视图
 * </p>
 *
 * @author chen
 * @since 2025-11-12
 */
@Getter
@Setter
@ToString
@Schema(name = "UserLoginVo", description = "用户登入视图")
public class UserLoginVo {
    @Schema(description = "用户id")
    private Long id;
    @Schema(description = "用户名称")
    private String username;
    @Schema(description = "token")
    private String token;
}
