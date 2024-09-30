package com.yeeiee.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TokenVo {
    private String accessToken;
    private String refreshToken;
}
