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
 * @since 2025-09-11
 */
@Getter
@Setter
@ToString
public class CodegenTableVo {
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "数据源名称")
    private String dataSource;

    @Schema(description = "表名称")
    private String tableName;

    @Schema(description = "表描述")
    private String tableComment;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
