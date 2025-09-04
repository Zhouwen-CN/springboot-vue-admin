package com.yeeiee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 角色选择器视图对象
 * </p>
 *
 * @author chen
 * @since 2024-09-30
 */
@Getter
@Setter
@ToString
@Schema(name = "RoleSelectorVo", description = "角色视图对象")
public class RoleSelectorVo {
    @Schema(description = "角色id")
    private Long id;
    @Schema(description = "角色名称")
    private String roleName;
}
