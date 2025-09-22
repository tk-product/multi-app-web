package com.recipe.web.core.config;

import com.recipe.domain.entity.Auth;
import com.recipe.domain.mapper.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthMapper authMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Auth auth = authMapper.findByEmail(email);
        if (auth == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<String> roles = authMapper.findRolesByUserId(auth.getUserId());
        List<String> permissions = authMapper.findPermissionsByUserId(auth.getUserId());

        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));

        return new CustomUserDetails(
                auth.getEmail(),
                auth.getUsername(),
                auth.getPassword(),
                authorities,
                permissions
        );
    }
}
