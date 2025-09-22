package com.recipe.web.app.common.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SessionScope
public class UserSession implements Serializable {
    private String email;
    private String username;
    private Collection<? extends GrantedAuthority> roles;
    private List<String> permissions;

}