package com.yeeiee.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RoleMenuIdsDto {
    private Long id;
    private String roleName;
    private String desc;
    private List<Long> menuIds;
}
