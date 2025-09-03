package com.yeeiee.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * 数据源配置表
 * </p>
 *
 * @author chen
 * @since 2025-08-22
 */
@Getter
@Setter
@ToString
@TableName("t_data_source")
@Schema(name = "DataSource", description = "数据源配置表")
public class DataSource {

    @Schema(description = "主键")
    @TableId(value = "id")
    private Long id;

    @Schema(description = "数据源名称")
    @TableField(value = "name")
    private String name;

    @Schema(description = "数据源连接")
    @TableField(value = "url")
    private String url;

    @Schema(description = "用户名")
    @TableField(value = "username")
    private String username;

    @Schema(description = "密码")
    @TableField(value = "password")
    private String password;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}