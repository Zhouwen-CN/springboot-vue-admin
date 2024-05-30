package com.yeeiee.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <p>
 * 新增 & 更新 角色信息
 * </p>
 *
 * @author chen
 * @since 2024/5/14
 */
@Getter
@Setter
@ToString
public class RoleMenuIdsDto {
    private Long id;
    private String roleName;
    private String desc;
    private List<Long> menuIds;
}
