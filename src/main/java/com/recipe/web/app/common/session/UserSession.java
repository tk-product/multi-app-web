package com.recipe.web.app.common.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSession implements Serializable {
    private String userId;
    private String userName;

    // 社内/社外などの大枠のrole
    private String role;

    // 権限レベル：ADMIN, EDIT, VIEW など
    private Set<String> permissionLevel;
}