package com.yeeiee.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * jwt 工具类
 * </p>
 *
 * @author chen
 * @since 2024-05-06
 */
@Slf4j
public final class JwtTokenUtil {
    private static final String SECRET_KEY = "ApZmvgBrjutc69dCBzLKBXNi4xc%CVjAxCyAc2Q@nM^SgNQyJS2H^$dq5fsj&vu#KAR6^!*Msqs4nM#^zFhY@AXbLBnMR*uyBS4P@L3#bUhBUqUYfQDujSHj9ZXaWcK2";
    private static final String AUTHORITIES = "authorities";
    private static final int EXPIRE = 1;

    public static String generateToken(Authentication authentication) {
        String username = authentication.getName();
        val authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, EXPIRE);

        val builder = JWT.create().withSubject(username);

        if (!authorities.isEmpty()) {
            builder.withClaim(AUTHORITIES, authorities);
        }

        return builder
                // 指定令牌过期时间
                .withExpiresAt(instance.getTime())
                // sign
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    /**
     * 获取token信息方法
     */
    public static Optional<String> getSubject(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build().verify(token);

            return Optional.ofNullable(jwt.getSubject());
        } catch (Exception e) {
            // do nothing
        }
        return Optional.empty();
    }

    public static Optional<List<String>> getAuths(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build().verify(token);
            return Optional.ofNullable(jwt.getClaim(AUTHORITIES).asList(String.class));
        } catch (Exception e) {
            // do nothing
        }
        return Optional.empty();
    }

    public static void main(String[] args) {
        val unauthenticated = UsernamePasswordAuthenticationToken.authenticated("admin", "admin", List.of(new SimpleGrantedAuthority("admin")));
        val token = generateToken(unauthenticated);
        System.out.println("token = " + token);
        val subject = getSubject(token);
        System.out.println("subject = " + subject);
        val auths = getAuths(token);
        System.out.println("auths = " + auths);
    }
}