package com.yeeiee.security.user;

import com.yeeiee.entity.User;
import com.yeeiee.exception.NamedAuthenticationException;
import com.yeeiee.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("Look up the data and verify whether the account password is correct");
        val username = (String) authentication.getPrincipal();
        val password = (String) authentication.getCredentials();

        val user = userService.lambdaQuery()
                .eq(User::getUsername, username)
                .one();

        if (user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new NamedAuthenticationException(username, "用户名或密码错误");
        }

        val token = new UserAuthenticationToken();
        token.setUser(user);
        token.setAuthenticated(true);

        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UserAuthenticationToken.class);
    }
}
