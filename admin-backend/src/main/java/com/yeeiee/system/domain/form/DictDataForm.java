package com.yeeiee.system.domain.form;

import com.yeeiee.system.domain.validate.GroupingValidate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 字典数据表单
 * </p>
 *
 * @author chen
 * @since 2025-05-27
 */
@Getter
@Setter
@ToString
@Schema(name = "DictDataForm", description = "字典数据表单")
public class DictDataForm {
    @NotNull(groups = {GroupingValidate.Update.class})
    @Schema(description = "字典数据主键")
    private Long id;

    @NotNull(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Schema(description = "字典类型id")
    private Long typeId;

    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, max = 15)
    @Schema(description = "字典键")
    private String label;

    @NotNull(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Schema(description = "字典值")
    private Integer data;

    @NotNull(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Schema(description = "字典排序")
    private Integer sortId;
}
