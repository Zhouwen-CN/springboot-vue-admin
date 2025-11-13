package com.yeeiee.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户视图
 * </p>
 *
 * @author chen
 * @since 2024-05-13
 */
@Getter
@Setter
@ToString
@Schema(name = "UserVo", description = "用户视图")
public class UserVo {
    @Schema(description = "用户id")
    private Long id;
    @Schema(description = "用户名称")
    private String username;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
