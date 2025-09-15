package com.recipe.web.core.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if (auth == null || !auth.isAuthenticated()) return false;
        String perm = (String) permission;
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(p -> p.equals(perm));
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}