package com.yeeiee.domain.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
public class LoginForm {
    private String username;
    private String password;
}
