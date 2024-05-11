package com.yeeiee.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 菜单 传输对象
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
@Getter
@Setter
@ToString
public class MenuDto {
    private Long id;
    private Integer pid;
    private String title;
    private String accessPath;
    private String filePath;
    private String icon;
    private LocalDateTime updateTime;
    private List<MenuDto> children;
}
