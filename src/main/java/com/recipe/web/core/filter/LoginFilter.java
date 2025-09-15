package com.recipe.web.core.filter;

import com.recipe.web.app.common.session.UserSession;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@Component
public class LoginFilter extends OncePerRequestFilter {

    @Value("${app.login.local-url:/local-login}")
    private String localLoginUrl;

    @Value("${app.login.external-url:https://auth.example.com/login}")
    private String externalLoginUrl;

    @Value("${spring.profiles.active:local}")
    private String activeProfile;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("★★★LoginFilter : ");
        HttpSession session = request.getSession(false);
        UserSession user = (session != null) ? (UserSession) session.getAttribute("USER_SESSION") : null;

        // 静的リソースやログイン関連URLは除外
        String path = request.getRequestURI();
        if (path.startsWith("/css") || path.startsWith("/js") || path.startsWith("/images")
                || path.startsWith("/local-login") || path.startsWith("/external-callback")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (user == null) {
            // 未ログイン → ログイン画面へリダイレクト
            String redirectUrl = ("local".equals(activeProfile)) ? localLoginUrl : externalLoginUrl;
            response.sendRedirect(redirectUrl + "?redirect=" + request.getRequestURI());
            return;
        }

        // ログイン済み → 通常処理
        filterChain.doFilter(request, response);
    }
}