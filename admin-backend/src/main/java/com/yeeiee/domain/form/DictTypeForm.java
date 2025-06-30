package com.yeeiee.domain.form;

import com.yeeiee.domain.entity.DictType;
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
    @NotNull(groups = {Update.class})
    @Schema(description = "字典类型id")
    private Long id;
    @NotBlank(groups = {Create.class, Update.class})
    @Length(groups = {Create.class, Update.class}, min = 1, max = 50)
    @Schema(description = "字典名称")
    private String name;
    @NotBlank(groups = {Create.class, Update.class})
    @Length(groups = {Create.class, Update.class}, min = 1, max = 50)
    @Schema(description = "字典类型")
    private String type;

    public interface Create {
    }

    public interface Update {

    }
}
