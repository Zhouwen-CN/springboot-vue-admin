package com.yeeiee.domain.form;

import com.yeeiee.domain.validate.GroupingValidate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * <p>
 * 学生表-代码生成测试表单
 * </p>
 *
 * @author chen
 * @since 2025-09-17
 */
@Getter
@Setter
@ToString
@Schema(name = "StudentTestForm", description = "学生表-代码生成测试表单")
public class StudentTestForm {

    @NotNull(groups = {GroupingValidate.Update.class})
    @Schema(description = "主键")
    private Long id;

    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, max = 20)
    @Schema(description = "名称")
    private String name;

    @NotNull(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Schema(description = "性别")
    private Integer gender;

    @NotNull(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Schema(description = "生日")
    private LocalDateTime birthday;

    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, max = 65535)
    @Schema(description = "简介")
    private String intro;

}
