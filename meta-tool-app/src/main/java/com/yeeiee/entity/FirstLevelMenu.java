package com.yeeiee.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 一级菜单表
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
@Getter
@Setter
@ToString
@TableName("`t_first_level_menu`")
@Schema(name = "FirstLevelMenu", description = "一级菜单表")
public class FirstLevelMenu {

    @Schema(description = "主键")
    @TableId(value = "`id`", type = IdType.AUTO)
    private Long id;

    @Schema(description = "标题")
    @TableField("`title`")
    private String title;

    @Schema(description = "访问路径")
    @TableField("`access_path`")
    private String accessPath;

    @Schema(description = "文件路径，为null则重定向到第一个子元素")
    @TableField("`file_path`")
    private String filePath;

    @Schema(description = "图标")
    @TableField("`icon`")
    private String icon;

    @Schema(description = "创建时间")
    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "`update_time`", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
