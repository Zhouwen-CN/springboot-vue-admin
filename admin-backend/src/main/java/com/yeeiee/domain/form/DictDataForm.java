package com.yeeiee.domain.form;

import com.yeeiee.domain.entity.DictData;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 新增 & 更新 字典数据表单
 * </p>
 *
 * @author chen
 * @since 2025-05-27
 */
@Getter
@Setter
@ToString
public class DictDataForm implements FormToBean<DictData> {
    @NotNull(groups = {Update.class})
    private Long id;
    @NotNull(groups = {Create.class, Update.class})
    private Long typeId;
    @NotBlank(groups = {Create.class, Update.class})
    @Length(groups = {Create.class, Update.class}, min = 1, max = 50)
    private String label;
    @NotNull(groups = {Create.class, Update.class})
    private Integer value;
    @NotNull(groups = {Create.class, Update.class})
    private Integer sort;

    public interface Create {
    }

    public interface Update {
    }
}
