package com.yeeiee.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
/**
 * <p>
 * 菜单列表，树形结构
 * </p>
 *
 * @author chen
 * @since 2024/5/14
 */
@Getter
@Setter
@ToString
public class MenuVo {
    private Long id;
    private String title;
    private String accessPath;
    private String filePath;
    private String icon;
    private Long pid;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<MenuVo> children;

    public static void mergeMenu(MenuVo dist, MenuVo src) {
        dist.setId(src.getId());
        dist.setTitle(src.getTitle());
        dist.setAccessPath(src.getAccessPath());
        dist.setFilePath(src.getFilePath());
        dist.setIcon(src.getIcon());
        dist.setPid(src.getPid());
        dist.setCreateTime(src.getCreateTime());
        dist.setUpdateTime(src.getUpdateTime());
        if (src.getChildren() != null) {
            dist.getChildren().addAll(src.getChildren());
        }
    }

    public MenuVo setChildren(List<MenuVo> children) {
        this.children = children;
        return this;
    }
}
