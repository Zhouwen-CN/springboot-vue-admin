package com.yeeiee.domain.form;

import com.yeeiee.domain.entity.DictData;
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
public class DictDataForm implements FormToBeanHelper<DictData> {
    @NotNull(groups = {Update.class})
    @Schema(description = "字典数据主键")
    private Long id;
    @NotNull(groups = {Create.class, Update.class})
    @Schema(description = "字典类型id")
    private Long typeId;
    @NotBlank(groups = {Create.class, Update.class})
    @Length(groups = {Create.class, Update.class}, min = 1, max = 50)
    @Schema(description = "字典键")
    private String label;
    @NotNull(groups = {Create.class, Update.class})
    @Schema(description = "字典值")
    private Integer value;
    @NotNull(groups = {Create.class, Update.class})
    @Schema(description = "字典排序")
    private Integer sort;

    public interface Create {
    }

    public interface Update {
    }
}
