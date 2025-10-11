package com.yeeiee.domain.form;

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
 * 菜单表单
 * </p>
 *
 * @author chen
 * @since 2025-10-11
 */
@Getter
@Setter
@ToString
@Schema(name = "MenuForm", description = "菜单表单")
public class MenuForm {

    @NotNull(groups = {GroupingValidate.Update.class})
    @Schema(description = "主键")
    private Long id;

    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, max = 15)
    @Schema(description = "标题")
    private String title;

    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, max = 40)
    @Pattern(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, regexp = "^/.*$")
    @Schema(description = "访问路径")
    private String accessPath;

    @NotBlank(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Length(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class}, max = 15)
    @Schema(description = "图标")
    private String icon;

    @NotNull(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Schema(description = "父级菜单id")
    private Long pid;

    @NotNull(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Schema(description = "是否缓存")
    private Boolean keepAlive;

    @NotNull(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Schema(description = "菜单类型：0-目录，1-菜单")
    private Integer menuType;

    @NotNull(groups = {GroupingValidate.Create.class, GroupingValidate.Update.class})
    @Schema(description = "菜单排序")
    private Integer sortId;

}