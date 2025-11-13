package com.yeeiee.system.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yeeiee.system.domain.serde.EncryptSerializer;
import com.yeeiee.system.domain.validate.FieldMatch;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "ChangePwdForm", description = "修改密码表单")
public class ChangePwdForm {
    @NotNull
    @Schema(description = "用户id")
    private Long id;

    @NotNull
    @Length(min = 5, max = 15)
    @Schema(description = "旧密码")
    private String oldPwd;

    @JsonSerialize(using = EncryptSerializer.class)
    @NotNull
    @Length(min = 5, max = 15)
    @Schema(description = "新密码")
    private String newPwd;

    @JsonSerialize(using = EncryptSerializer.class)
    @NotNull
    @Length(min = 5, max = 15)
    @Schema(description = "确认密码")
    private String confirmPwd;
}
