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
    private Set<String> roles; // 例: ADMIN, EDITOR, VIEWER
}