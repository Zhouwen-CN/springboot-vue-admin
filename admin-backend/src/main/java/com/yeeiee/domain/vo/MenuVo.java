package com.yeeiee.domain.vo;

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
 * @since 2024-05-14
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
    private Boolean keepAlive;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<MenuVo> children;
}
