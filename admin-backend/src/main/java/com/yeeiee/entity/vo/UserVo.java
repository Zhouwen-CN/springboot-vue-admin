package com.yeeiee.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <p>
 * 登入成功返回对象
 * </p>
 *
 * @author chen
 * @since 2025/11/12
 */
@Getter
@Setter
@ToString
public class UserVo {
    private Long id;
    private String username;
    private String accessToken;
    private String refreshToken;
    private List<RoleVo> roleList;
}
