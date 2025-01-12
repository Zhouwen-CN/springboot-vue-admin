package com.yeeiee.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.springframework.util.StringUtils;

import java.util.Calendar;
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
public final class JwtTokenUtil {
    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private static final String ACCESS_SECRET_KEY = "6&beRHT#gKZOF0%9$A5t";
    private static final String REFRESH_SECRET_KEY = "MHy7K9Cmlf#AwNDXp3&h";

    private static final int ACCESS_EXPIRATION_HOUR = 1;
    private static final int REFRESH_EXPIRATION_DAY = 7;

    private static String generateToken(String username, Long version, Calendar calendar, String secretKey) {
        return JWT.create()
                .withClaim("username", username)
                .withClaim("version", version)
                // 指定令牌过期时间
                .withExpiresAt(calendar.getTime())
                // sign
                .sign(Algorithm.HMAC256(secretKey));
    }

    public static String generateAccessToken(String username, Long version) {
        val instance = Calendar.getInstance();
        instance.add(Calendar.HOUR, ACCESS_EXPIRATION_HOUR);
        return generateToken(username, version, instance, ACCESS_SECRET_KEY);
    }

    public static String generateRefreshToken(String username, Long version) {
        val instance = Calendar.getInstance();
        instance.add(Calendar.DATE, REFRESH_EXPIRATION_DAY);
        return generateToken(username, version, instance, REFRESH_SECRET_KEY);
    }

    private static Optional<Map<String, Claim>> parseToken(String token, String secretKey) {
        try {
            val jwt = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
            return Optional.of(jwt.getClaims());
        } catch (Exception e) {
            // do noting
            return Optional.empty();
        }
    }

    public static Optional<Map<String, Claim>> parseAccessToken(String token) {
        return parseToken(token, ACCESS_SECRET_KEY);
    }

    public static Optional<Map<String, Claim>> parseRefreshToken(String token) {
        return parseToken(token, REFRESH_SECRET_KEY);
    }

    public static String getTokenFromRequest(HttpServletRequest request) {
        val bearerToken = request.getHeader(HEADER_STRING);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}