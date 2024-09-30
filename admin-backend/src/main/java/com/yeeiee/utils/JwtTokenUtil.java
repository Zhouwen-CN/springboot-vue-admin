package com.yeeiee.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import lombok.val;

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
    private static final String SECRET_KEY = "ApZmvgBrjutc69dCBzLKBXNi4xc%CVjAxCyAc2Q@nM^SgNQyJS2H^$dq5fsj&vu#KAR6^!*Msqs4nM#^zFhY@AXbLBnMR*uyBS4P@L3#bUhBUqUYfQDujSHj9ZXaWcK2";
    private static final int EXPIRATION = 1;

    public static String generateToken(String username, Long version) {
        val instance = Calendar.getInstance();
        instance.add(Calendar.DATE, EXPIRATION);

        return JWT.create()
                .withClaim("username", username)
                .withClaim("version", version)
                // 指定令牌过期时间
                .withExpiresAt(instance.getTime())
                // sign
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public static Optional<Map<String, Claim>> getClaims(String token) {
        try {
            val jwt = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build().verify(token);
            return Optional.ofNullable(jwt.getClaims());
        } catch (Exception e) {
            // do nothing
        }
        return Optional.empty();
    }
}