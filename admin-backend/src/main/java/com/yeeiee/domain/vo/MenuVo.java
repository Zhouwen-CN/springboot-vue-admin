package com.yeeiee.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 菜单视图对象
 * </p>
 *
 * @author chen
 * @since 2024-05-14
 */
@Getter
@Setter
@ToString
@Schema(name = "MenuVo", description = "菜单视图对象")
public class MenuVo {
    @Schema(description = "菜单id")
    private Long id;
    @Schema(description = "菜单标题")
    private String title;
    @Schema(description = "菜单访问路径")
    private String accessPath;
    @Schema(description = "菜单文件路径")
    private String filePath;
    @Schema(description = "菜单图标")
    private String icon;
    @Schema(description = "父级菜单id")
    private Long pid;
    @Schema(description = "是否缓存")
    private Boolean keepAlive;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    @Schema(description = "子菜单列表")
    private List<MenuVo> children;
}
