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
 * @since 2025-02-11
 */
@Getter
@Setter
@ToString
@TableName("`t_menu`")
@Schema(name = "Menu", description = "菜单表")
public class Menu {

    @Schema(description = "主键")
    @TableId(value = "`id`", type = IdType.AUTO)
    private Long id;

    @Schema(description = "标题")
    @TableField("`title`")
    private String title;

    @Schema(description = "访问路径")
    @TableField("`access_path`")
    private String accessPath;

    @Schema(description = "文件路径")
    @TableField("`file_path`")
    private String filePath;

    @Schema(description = "图标")
    @TableField("`icon`")
    private String icon;

    @Schema(description = "父级菜单id")
    @TableField("`pid`")
    private Long pid;

    @Schema(description = "是否缓存")
    @TableField("`is_keep_alive`")
    private Boolean keepAlive;

    @Schema(description = "创建时间")
    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "`update_time`", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
