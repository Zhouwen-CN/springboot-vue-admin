package com.yeeiee.system.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <p>
 * jwt dto
 * </p>
 *
 * @author chen
 * @since 2025-11-11
 */
@Getter
@Setter
@ToString
public class JwtClaimsDto {
    private Long userId;
    private Long version;
    private List<String> roleNames;
}
