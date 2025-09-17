package com.yeeiee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 学生表-代码生成测试视图
 * </p>
 *
 * @author chen
 * @since 2025-09-17
 */
@Getter
@Setter
@ToString
@Schema(name = "StudentTestVo", description = "学生表-代码生成测试视图")
public class StudentTestVo {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "生日")
    private LocalDateTime birthday;

    @Schema(description = "简介")
    private String intro;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}