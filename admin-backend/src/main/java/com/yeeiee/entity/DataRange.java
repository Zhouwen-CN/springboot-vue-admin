package com.yeeiee.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 数据范围表
 * </p>
 *
 * @author chen
 * @since 2024-04-28
 */
@Getter
@Setter
@ToString
@TableName("`t_data_range`")
@Schema(name = "DataRange", description = "数据范围表")
public class DataRange {

    @Schema(description = "主键")
    @TableId(value = "`id`", type = IdType.AUTO)
    private Long id;

    @Schema(description = "范围/周期")
    @TableField("`range`")
    private String range;

    @Schema(description = "说明")
    @TableField("`desc`")
    private String desc;

    @Schema(description = "创建时间")
    @TableField(value = "`create_time`", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "`update_time`", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
