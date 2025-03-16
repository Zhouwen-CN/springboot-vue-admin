package com.yeeiee.entity.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yeeiee.config.EncryptSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <p>
 * 新增 & 更新 用户信息
 * </p>
 *
 * @author chen
 * @since 2024-05-14
 */
@Getter
@Setter
@ToString
public class UserRoleIdsDto {
    private Long id;
    private String username;
    @JsonSerialize(using = EncryptSerializer.class)
    private String password;
    private List<Long> roleIds;
}
