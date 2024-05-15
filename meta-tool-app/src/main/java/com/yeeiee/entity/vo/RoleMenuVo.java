package com.yeeiee.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class RoleMenuVo {
    private Long id;
    private String roleName;
    private String desc;
    private String menuIds;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
