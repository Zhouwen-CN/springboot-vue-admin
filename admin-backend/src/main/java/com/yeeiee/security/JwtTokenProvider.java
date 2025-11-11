package com.yeeiee.security;

import com.yeeiee.domain.dto.JwtClaimsDto;
import com.yeeiee.exception.AiChatException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * <p>
 * token提供者
 * </p>
 *
 * @author chen
 * @since 2025-06-04
 */

@RequiredArgsConstructor
@Component
@ConfigurationProperties(prefix = "custom.jwt")
public class JwtTokenProvider implements InitializingBean {
    @Setter
    @Getter
    private AccessTokenConfig access;
    @Setter
    @Getter
    private RefreshTokenConfig refresh;

    @Getter
    @Setter
    public static class AccessTokenConfig {
        /**
         * 访问token密钥
         */
        private String key;
        /**
         * 访问token过期时间
         */
        private Duration expiration;
    }

    @Getter
    @Setter
    public static class RefreshTokenConfig {
        /**
         * 刷新token密钥
         */
        private String key;
        /**
         * 刷新token过期时间
         */
        private Duration expiration;
    }

    private SecretKey accessSecretKey;
    private SecretKey refreshSecretKey;

    @Override
    public void afterPropertiesSet() throws Exception {
        accessSecretKey = Keys.hmacShaKeyFor(this.access.key.getBytes(StandardCharsets.UTF_8));
        refreshSecretKey = Keys.hmacShaKeyFor(this.refresh.key.getBytes(StandardCharsets.UTF_8));
    }

    @SneakyThrows
    private String generateToken(Long userId, List<String> roles, Long tokenVersion, SecretKey secretKey, Duration expiration) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration.toMillis());

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .issuer("sv-admin")
                .expiration(expiryDate)
                .claim("roles", roles)
                .claim("version", tokenVersion)
                .signWith(secretKey)
                .compact();
    }

    public String generateAccessToken(Long userId, List<String> roles, Long tokenVersion) {
        return generateToken(userId, roles, tokenVersion, accessSecretKey, this.access.expiration);
    }

    public String generateRefreshToken(Long userId, List<String> roles, Long tokenVersion) {
        return generateToken(userId, roles, tokenVersion, refreshSecretKey, this.refresh.expiration);
    }

    private Optional<Jws<Claims>> validateToken(String token, SecretKey secretKey) {
        try {
            val claimsJws = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return Optional.of(claimsJws);
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public Optional<Jws<Claims>> parseAccessToken(String token) {
        return validateToken(token, accessSecretKey);
    }

    public Optional<Jws<Claims>> parseRefreshToken(String token) {
        return validateToken(token, refreshSecretKey);
    }

    @SuppressWarnings("unchecked")
    private Function<Jws<Claims>, JwtClaimsDto> getClaimsDtoFunction() {
        return claimsJws -> {
            val payload = claimsJws.getPayload();
            val jwtClaimsDto = new JwtClaimsDto();
            jwtClaimsDto.setUserId(Long.valueOf(payload.getSubject()));
            jwtClaimsDto.setVersion(payload.get("version", Long.class));
            jwtClaimsDto.setRoleNames(payload.get("roles", List.class));
            return jwtClaimsDto;
        };
    }

    private String trimToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

    public Optional<JwtClaimsDto> getClaimsDtoByAccessToken(String token) {
        token = trimToken(token);
        return this.parseAccessToken(token)
                .map(this.getClaimsDtoFunction());
    }

    public Optional<JwtClaimsDto> getClaimsDtoByRefreshToken(String token) {
        token = trimToken(token);
        return this.parseRefreshToken(token)
                .map(this.getClaimsDtoFunction());
    }

    public Long getUserIdByAccessToken(String token) {
        return this.getClaimsDtoByAccessToken(token)
                .map(JwtClaimsDto::getUserId)
                .orElseThrow(() -> new AiChatException("token解析失败"));
    }
}
