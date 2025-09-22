package com.recipe.web.core.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final String email;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities; // ロールのみ
    private final List<String> permissions; // パーミッションは別管理

}
