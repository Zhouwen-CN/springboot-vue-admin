package com.yeeiee.domain.form;

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
 * @since 2024-05-14
 */
@Getter
@Setter
@ToString
public class RoleMenuIdsForm {
    private Long id;
    private String roleName;
    private String desc;
    private List<Long> menuIds;
}
