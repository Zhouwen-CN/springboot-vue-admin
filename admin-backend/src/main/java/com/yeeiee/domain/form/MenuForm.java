package com.yeeiee.domain.form;

import com.yeeiee.domain.entity.Menu;
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
public class MenuForm implements FormToBean<Menu> {
    @NotNull(groups = {Update.class})
    private Long id;
    @NotBlank(groups = {Create.class, Update.class})
    @Length(groups = {Create.class, Update.class}, min = 4, max = 15)
    private String title;
    @Pattern(groups = {Create.class, Update.class}, regexp = "^/.*$")
    private String accessPath;
    private String filePath;
    @NotBlank(groups = {Create.class, Update.class})
    private String icon;
    @NotNull(groups = {Create.class, Update.class})
    private Long pid;
    @NotNull(groups = {Create.class, Update.class})
    private Boolean keepAlive;

    public interface Create {
    }

    public interface Update {
    }
}
