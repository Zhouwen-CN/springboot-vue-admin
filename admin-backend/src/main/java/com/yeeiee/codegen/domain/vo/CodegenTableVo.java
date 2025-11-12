package com.yeeiee.codegen.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 代码生成表视图
 * </p>
 *
 * @author chen
 * @since 2025-09-11
 */
@Getter
@Setter
@ToString
@Schema(name = "CodegenTableVo", description = "代码生成表视图")
public class CodegenTableVo {
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "数据源名称")
    private String dataSource;

    @Schema(description = "上级菜单编号")
    private Long parentMenuId;

    @Schema(description = "表名称")
    private String tableName;

    @Schema(description = "表描述")
    private String tableComment;

    @Schema(description = "类名称")
    private String className;

    @Schema(description = "java基础包名")
    private String javaBasePackage;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "业务名称")
    private String businessName;

    @Schema(description = "忽略表前缀")
    private String ignoreTablePrefix;

    @Schema(description = "忽略字段前缀")
    private String ignoreColumnPrefix;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
