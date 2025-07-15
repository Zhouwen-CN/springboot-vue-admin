package com.yeeiee.domain.form;

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
 * 新增 & 更新 角色表单
 * </p>
 *
 * @author chen
 * @since 2024-05-14
 */
@Getter
@Setter
@ToString
@Schema(name = "RoleForm", description = "角色表单")
public class RoleForm {
    @NotNull(groups = {Update.class})
    @Schema(description = "角色id")
    private Long id;
    @NotBlank(groups = {Create.class, Update.class})
    @Length(groups = {Create.class, Update.class}, min = 3, max = 15)
    @Schema(description = "角色名称")
    private String roleName;
    @NotBlank(groups = {Create.class, Update.class})
    @Length(groups = {Create.class, Update.class}, min = 1, max = 40)
    @Schema(description = "角色描述")
    private String desc;

    public interface Create {
    }

    public interface Update {
    }

    private List<Long> menuIds;
}
