package com.yeeiee.domain.form;

import com.yeeiee.domain.entity.Menu;
import com.yeeiee.domain.validate.GroupingValidate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * <p>
 * 新增 & 更新 菜单表单
 * </p>
 *
 * @author chen
 * @since 2025-05-27
 */
@Getter
@Setter
@ToString
@Schema(name = "MenuForm", description = "菜单表单")
public class MenuForm implements FormToBeanHelper<Menu> {
    @NotNull(groups = {GroupingValidate.Update.class})
    @Schema(description = "菜单id")
    private Long id;
    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, min = 4, max = 15)
    @Schema(description = "菜单标题")
    private String title;
    @Pattern(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, regexp = "^/.*$")
    @Schema(description = "菜单访问路径")
    private String accessPath;
    @Schema(description = "菜单文件路径")
    private String filePath;
    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Schema(description = "菜单图标")
    private String icon;
    @NotNull(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Schema(description = "父级菜单id")
    private Long pid;
    @NotNull(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Schema(description = "是否缓存")
    private Boolean keepAlive;
}
