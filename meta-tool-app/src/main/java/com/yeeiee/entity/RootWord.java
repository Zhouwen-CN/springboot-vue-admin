package com.yeeiee.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 词根表
 * </p>
 *
 * @author chen
 * @since 2024-04-27
 */
@Getter
@Setter
@TableName("`t_root_word`")
@Schema(name = "RootWord", description = "词根表")
public class RootWord implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "`id`", type = IdType.AUTO)
    private Long id;

    @Schema(description = "单词")
    @TableField("`word`")
    private String word;

    @Schema(description = "说明")
    @TableField("`desc`")
    private String desc;
}
