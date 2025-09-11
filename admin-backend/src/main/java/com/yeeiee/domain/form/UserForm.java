package com.yeeiee.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yeeiee.domain.entity.User;
import com.yeeiee.domain.serde.EncryptSerializer;
import com.yeeiee.domain.validate.GroupingValidate;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "UserForm", description = "用户表单")
public class UserForm implements FormToBeanHelper<User> {
    @NotNull(groups = {GroupingValidate.Update.class})
    @Schema(description = "用户id")
    private Long id;

    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, min = 5, max = 15)
    @Schema(description = "用户名称")
    private String username;

    @JsonSerialize(using = EncryptSerializer.class)
    @NotBlank(groups = {GroupingValidate.Create.class})
    @Length(groups = {GroupingValidate.Create.class}, min = 5, max = 15)
    @Schema(description = "用户密码")
    private String password;

    @Schema(description = "角色id列表")
    private List<Long> roleIds;
}
