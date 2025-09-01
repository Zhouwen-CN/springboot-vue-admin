package com.yeeiee.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 用户登入表单
 * </p>
 *
 * @author chen
 * @since 2024-05-07
 */
@Getter
@Setter
@ToString
@Schema(name = "LoginForm", description = "用户登入表单")
public class LoginForm {
    @Length(min = 5, max = 15)
    @Schema(description = "用户名称")
    private String username;
    @Length(min = 5, max = 15)
    @Schema(description = "用户密码")
    private String password;
}
