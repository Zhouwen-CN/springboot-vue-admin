package com.yeeiee.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.val;
import org.springframework.security.core.Authentication;

import java.util.Calendar;
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
    private static final String SECRET_KEY = "ApZmvgBrjutc69dCBzLKBXNi4xc%CVjAxCyAc2Q@nM^SgNQyJS2H^$dq5fsj&vu#KAR6^!*Msqs4nM#^zFhY@AXbLBnMR*uyBS4P@L3#bUhBUqUYfQDujSHj9ZXaWcK2";
    private static final int EXPIRATION = 1;

    public static String generateToken(Authentication authentication) {
        val username = authentication.getName();

        val instance = Calendar.getInstance();
        instance.add(Calendar.DATE, EXPIRATION);

        return JWT.create()
                .withSubject(username)
                .withClaim("version", 1)
                // 指定令牌过期时间
                .withExpiresAt(instance.getTime())
                // sign
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public static Optional<String> getSubject(String token) {
        try {
            val jwt = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build().verify(token);
            return Optional.ofNullable(jwt.getSubject());
        } catch (Exception e) {
            // do nothing
        }
        return Optional.empty();
    }
}