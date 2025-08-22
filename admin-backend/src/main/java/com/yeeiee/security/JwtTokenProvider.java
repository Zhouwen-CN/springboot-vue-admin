package com.yeeiee.security;

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
    public static class AccessTokenConfig{
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
    public static class RefreshTokenConfig{
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
    private String generateToken(String username, List<String> roles, Long tokenVersion, SecretKey secretKey, Duration expiration) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration.toMillis());

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .issuer("sv-admin")
                .expiration(expiryDate)
                .claim("roles", roles)
                .claim("version", tokenVersion)
                .signWith(secretKey)
                .compact();
    }

    public String generateAccessToken(String username, List<String> roles, Long tokenVersion) {
        return generateToken(username, roles, tokenVersion, accessSecretKey, this.access.expiration);
    }

    public String generateRefreshToken(String username, List<String> roles, Long tokenVersion) {
        return generateToken(username, roles, tokenVersion, refreshSecretKey, this.refresh.expiration);
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
    public List<String> getRoles(Claims payload) {
        return (List<String>) payload.get("roles", List.class);
    }
}
