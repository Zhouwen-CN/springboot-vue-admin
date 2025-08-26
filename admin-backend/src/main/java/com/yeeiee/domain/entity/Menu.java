package com.yeeiee.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author chen
 * @since 2025-08-26
 */
@Getter
@Setter
@ToString
@TableName("t_menu")
@Schema(name = "Menu", description = "菜单表")
public class Menu {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "标题")
    @TableField(value = "title", keepGlobalFormat = true)
    private String title;

    @Schema(description = "访问路径")
    @TableField(value = "access_path", keepGlobalFormat = true)
    private String accessPath;

    @Schema(description = "文件路径")
    @TableField(value = "file_path", keepGlobalFormat = true)
    private String filePath;

    @Schema(description = "图标")
    @TableField(value = "icon", keepGlobalFormat = true)
    private String icon;

    @Schema(description = "父级菜单id")
    @TableField(value = "pid", keepGlobalFormat = true)
    private Long pid;

    @Schema(description = "是否缓存")
    @TableField(value = "is_keep_alive", keepGlobalFormat = true)
    private Boolean keepAlive;

    @Schema(description = "菜单类型：0-目录，1-菜单")
    @TableField(value = "type", keepGlobalFormat = true)
    private Integer type;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT, keepGlobalFormat = true)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE, keepGlobalFormat = true)
    private LocalDateTime updateTime;
}