package com.yeeiee.system.domain.form;

import com.yeeiee.system.domain.validate.GroupingValidate;
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
    @NotNull(groups = {GroupingValidate.Update.class})
    @Schema(description = "角色id")
    private Long id;

    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, min = 3, max = 15)
    @Schema(description = "角色名称")
    private String roleName;

    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, min = 1, max = 40)
    @Schema(description = "角色描述")
    private String description;

    @Schema(description = "菜单id列表")
    private List<Long> menuIds;
}
