package com.recipe.web.app.common.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSession implements Serializable {
    private String userId;
    private String userName;

    // 社内/社外などの大枠のrole
    private List<String> roles;

}