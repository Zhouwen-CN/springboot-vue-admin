package com.yeeiee.domain.form;

import com.yeeiee.domain.entity.DictType;
import com.yeeiee.domain.validate.GroupingValidate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 字典类型表单
 * </p>
 *
 * @author chen
 * @since 2025-05-27
 */
@Getter
@Setter
@ToString
@Schema(name = "DictTypeForm", description = "字典类型表单")
public class DictTypeForm implements FormToBeanHelper<DictType> {
    @NotNull(groups = {GroupingValidate.Update.class})
    @Schema(description = "字典类型id")
    private Long id;

    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, max = 40)
    @Schema(description = "字典名称")
    private String name;

    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, max = 15)
    @Schema(description = "字典类型")
    private String dictType;
}
