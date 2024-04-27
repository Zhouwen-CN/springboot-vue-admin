package com.yeeiee.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 数据域表
 * </p>
 *
 * @author chen
 * @since 2024-04-27
 */
@Getter
@Setter
@TableName("t_data_field")
@Schema(name = "DataField", description = "数据域表")
public class DataField implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "领域名称")
    @TableField("name")
    private String name;

    @Schema(description = "说明")
    @TableField("description")
    private String description;
}
