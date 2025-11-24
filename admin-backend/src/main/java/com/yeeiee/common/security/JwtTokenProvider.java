package com.yeeiee.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * token提供者
 * </p>
 *
 * @author chen
 * @since 2025-11-19
 */

@RequiredArgsConstructor
@Component
public class JwtTokenProvider implements InitializingBean {
    /**
     * token密钥
     */
    @Value("${custom.jwt.key}")
    private String key;
    /**
     * 访问token过期时间
     */
    @Value("${custom.jwt.access-expiration}")
    private Duration accessTokenExpiration;
    /**
     * 刷新token过期时间
     */
    @Value("${custom.jwt.refresh-expiration}")
    private Duration refreshTokenExpiration;

    private SecretKey secretKey;

    @Override
    public void afterPropertiesSet() throws Exception {
        secretKey = Keys.hmacShaKeyFor(this.key.getBytes(StandardCharsets.UTF_8));
    }

    @SneakyThrows
    private String generateToken(Long userId, List<String> roles, SecretKey secretKey, Duration expiration) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration.toMillis());

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuer("sv-admin")
                .issuedAt(now)
                .expiration(expiryDate)
                .claim("roles", roles)
                .signWith(secretKey)
                .compact();
    }

    public String generateAccessToken(Long userId, List<String> roles) {
        return generateToken(userId, roles, secretKey, accessTokenExpiration);
    }

    public String generateRefreshToken(Long userId, List<String> roles) {
        return generateToken(userId, roles, secretKey, refreshTokenExpiration);
    }

    /**
     * 删除token前缀，如何存在的话
     *
     * @param token token
     * @return token
     */
    private String removePrefixIfExists(String token) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

    /**
     * 解析token并获取claims，任何异常将返回空
     *
     * @param token token
     * @return claims
     */
    public Optional<Claims> getClaims(String token) {
        try {
            return Optional.of(
                    Jwts.parser()
                            .verifyWith(secretKey)
                            .build()
                            .parseSignedClaims(this.removePrefixIfExists(token))
                            .getPayload()
            );
        } catch (Exception e) {
            // do noting
        }

        return Optional.empty();
    }

    /**
     * 解析token并获取claims，忽略token过期
     *
     * @param token token
     * @return claims
     */
    public Optional<Claims> getClaimsIgnoreExpired(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(this.removePrefixIfExists(token))
                    .getPayload();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        } catch (Exception e) {
            // do noting
        }
        return Optional.ofNullable(claims);
    }

    public List<String> getRoleNames(Claims claims) {
        return claims.get("roles", List.class);
    }
}
