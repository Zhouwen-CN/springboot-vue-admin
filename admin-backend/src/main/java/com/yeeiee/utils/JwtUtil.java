package com.yeeiee.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * jwt 工具类
 * </p>
 *
 * @author chen
 * @since 2024-05-06
 */
@Component
public final class JwtUtil {
    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Value("${jwt.access.key}")
    private String accessKey;
    @Value("${jwt.access.expiration}")
    private Duration accessExpiration;
    @Value("${jwt.refresh.key}")
    private String refreshKey;
    @Value("${jwt.refresh.expiration}")
    private Duration refreshExpiration;

    private String generateToken(String username, List<String> roleNames, Long version, Calendar calendar, String secretKey) {
        return JWT.create()
                .withClaim("username", username)
                .withClaim("roleNames", roleNames)
                .withClaim("version", version)
                // 指定令牌过期时间
                .withExpiresAt(calendar.getTime())
                // sign
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String generateAccessToken(String username, List<String> roleNames, Long version) {
        val instance = Calendar.getInstance();
        instance.setTimeInMillis(instance.getTimeInMillis() + accessExpiration.toMillis());
        return generateToken(username, roleNames, version, instance, accessKey);
    }

    public String generateRefreshToken(String username, List<String> roleNames, Long version) {
        val instance = Calendar.getInstance();
        instance.setTimeInMillis(instance.getTimeInMillis() + refreshExpiration.toMillis());
        return generateToken(username, roleNames, version, instance, refreshKey);
    }

    private Optional<Map<String, Claim>> parseToken(String token, String secretKey) {
        try {
            val jwt = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
            return Optional.of(jwt.getClaims());
        } catch (Exception e) {
            // do noting
            return Optional.empty();
        }
    }

    public Optional<Map<String, Claim>> parseAccessToken(String token) {
        return parseToken(token, accessKey);
    }

    public Optional<Map<String, Claim>> parseRefreshToken(String token) {
        return parseToken(token, refreshKey);
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        val bearerToken = request.getHeader(HEADER_STRING);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}