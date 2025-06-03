package com.yeeiee.domain.form;

import com.yeeiee.domain.entity.DictType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 新增 & 更新 字典类型表单
 * </p>
 *
 * @author chen
 * @since 2025-05-27
 */
@Getter
@Setter
@ToString
public class DictTypeForm implements FormToBean<DictType> {
    @NotNull(groups = {Update.class})
    private Long id;
    @NotBlank(groups = {Create.class, Update.class})
    @Length(groups = {Create.class, Update.class}, min = 1, max = 50)
    private String name;
    @NotBlank(groups = {Create.class, Update.class})
    @Length(groups = {Create.class, Update.class}, min = 1, max = 50)
    private String type;

    public interface Create {
    }

    public interface Update {

    }
}
