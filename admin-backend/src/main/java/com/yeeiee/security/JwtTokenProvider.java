package com.yeeiee.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 *
 * </p>
 *
 * @author chen
 * @since 2025-06-04
 */
@RequiredArgsConstructor
@Component
public class JwtTokenProvider implements InitializingBean {
    @Value("${jwt.access.key}")
    private String accessKey;
    @Value("${jwt.access.expiration}")
    private Duration accessExpiration;
    @Value("${jwt.refresh.key}")
    private String refreshKey;
    @Value("${jwt.refresh.expiration}")
    private Duration refreshExpiration;
    private SecretKey accessSecretKey;
    private SecretKey refreshSecretKey;

    @Override
    public void afterPropertiesSet() throws Exception {
        accessSecretKey = Keys.hmacShaKeyFor(accessKey.getBytes(StandardCharsets.UTF_8));
        refreshSecretKey = Keys.hmacShaKeyFor(refreshKey.getBytes(StandardCharsets.UTF_8));
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
        return generateToken(username, roles, tokenVersion, accessSecretKey, accessExpiration);
    }

    public String generateRefreshToken(String username, List<String> roles, Long tokenVersion) {
        return generateToken(username, roles, tokenVersion, refreshSecretKey, refreshExpiration);
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
