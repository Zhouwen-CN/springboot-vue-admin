package com.yeeiee.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yeeiee.config.EncryptSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * <p>
 * 新增 & 更新 用户表单
 * </p>
 *
 * @author chen
 * @since 2024-05-14
 */
@Getter
@Setter
@ToString
public class UserForm {
    @NotNull(groups = {Update.class})
    private Long id;
    @NotBlank(groups = {Create.class, Update.class})
    @Length(groups = {Create.class, Update.class}, min = 5, max = 15)
    private String username;
    @JsonSerialize(using = EncryptSerializer.class)
    @NotBlank(groups = {Create.class})
    @Length(groups = {Create.class}, min = 5)
    private String password;

    public interface Update {
    }

    public interface Create {
    }

    private List<Long> roleIds;
}
