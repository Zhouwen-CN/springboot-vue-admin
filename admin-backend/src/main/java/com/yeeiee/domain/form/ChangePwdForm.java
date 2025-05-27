package com.yeeiee.domain.form;

import com.yeeiee.domain.validate.FieldMatch;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 修改密码表单
 * </p>
 *
 * @author chen
 * @since 2025-05-27
 */
@Getter
@Setter
@ToString
@FieldMatch(
        field = "newPwd",
        confirmField = "confirmPwd",
        message = "两次输入的密码不一致"
)
public class ChangePwdForm {
    @NotNull
    private Long id;
    @Length(min = 5, max = 15)
    private String oldPwd;
    @Length(min = 5, max = 15)
    private String newPwd;
    @Length(min = 5, max = 15)
    private String confirmPwd;
}
