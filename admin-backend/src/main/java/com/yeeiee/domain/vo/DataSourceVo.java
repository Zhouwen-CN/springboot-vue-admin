package com.yeeiee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author chen
 * @since 2025-08-22
 */
@Getter
@Setter
@ToString
public class DataSourceVo {
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "数据源名称")
    private String name;
    @Schema(description = "数据源连接")
    private String url;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
