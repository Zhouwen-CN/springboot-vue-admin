package com.yeeiee.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private List<MenuVo> children = new ArrayList<>();

    public static void mergeMenu(MenuVo dest, MenuVo src) {
        if (Objects.equals(dest.getId(), src.getId())) {
            return;
        }
        dest.setId(src.getId());
        dest.setTitle(src.getTitle());
        dest.setAccessPath(src.getAccessPath());
        dest.setFilePath(src.getFilePath());
        dest.setIcon(src.getIcon());
        dest.setPid(src.getPid());
        dest.setCreateTime(src.getCreateTime());
        dest.setUpdateTime(src.getUpdateTime());
        dest.getChildren().addAll(src.getChildren());
    }
}
