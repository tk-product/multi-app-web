package com.recipe.web.core.filter;

import com.recipe.web.app.common.session.UserSession;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);
    private static final String REQUEST_ID_HEADER = "X-Request-Id";
    private HttpServletRequest request;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        // `OPTIONS` リクエストの処理
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        }

        // キャッシュ無効
        wrappedResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        String requestId = UUID.randomUUID().toString();
        // MDC (Mapped Diagnostic Context) にリクエストIDを保存（ロギングに利用できる）
        MDC.put(REQUEST_ID_HEADER, requestId);

        // **リクエストのログをリクエスト到着時に出力**
        logRequest(wrappedRequest, requestId);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            // **レスポンスのログをレスポンス送信時に出力**
            logResponse(wrappedResponse, requestId);
            // キャッシュを元に戻す
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request, String requestId) throws UnsupportedEncodingException {
        String sessionId = request.getSession().getId();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String secondDecode = "";
        if (request.getQueryString() != null) {
            // 最初のデコード
            String firstDecode = URLDecoder.decode(request.getQueryString(), StandardCharsets.UTF_8);
            // 二度目のデコード
            secondDecode = URLDecoder.decode(firstDecode, StandardCharsets.UTF_8);

        }
        String headers = getRequestHeaders(request);
        String requestBody = getRequestBody(request);
        logger.info("SessionID: {}, RequestID: {}, Request URI: {} {}{}", sessionId, requestId, method, uri, (secondDecode != null ? "?" + secondDecode : ""));
        logger.info("SessionID: {}, RequestID: {}, Request Headers: {}", sessionId, requestId, headers);
        logger.info("SessionID: {}, RequestID: {}, Request Body: {}", sessionId, requestId, requestBody);
    }

    private void logResponse(ContentCachingResponseWrapper response, String requestId) {
        String sessionId = request.getSession().getId();
        int status = response.getStatus();
        String headers = getResponseHeaders(response);
        String responseBody = getResponseBody(response);

        // 認証後ログ
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        List<String> roles = List.of();
        List<String> permissions = List.of();
        if (auth != null) {
            username = auth.getName();
            roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
        }
        UserSession userSession = new UserSession();
        userSession.setUserName(username);
        userSession.setRole(roles.getFirst());
        userSession.setPermissionLevel(username);
        logger.info("SessionID: {}, RequestID: {}, Response Status: {}", sessionId, requestId, status);
        logger.info("SessionID: {}, RequestID: {}, Response Headers: {}", sessionId, requestId, headers);
        logger.info("SessionID: {}, RequestID: {}, UserName: {}, ROLE: {}, PERMISSION: {}", sessionId, requestId, username, roles);
    }

    private String getRequestHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames == null) {
            return "";
        }
        return Collections.list(headerNames).stream()
                .map(header -> header + ": " + request.getHeader(header))
                .collect(Collectors.joining(", "));
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        return content.length > 0 ? new String(content, StandardCharsets.UTF_8) : "";
    }

    private String getResponseHeaders(HttpServletResponse response) {
        Collection<String> headerNames = response.getHeaderNames();
        if (headerNames == null || headerNames.isEmpty()) {
            return "";
        }
        return headerNames.stream()
                .map(header -> header + ": " + response.getHeader(header))
                .collect(Collectors.joining(", "));
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        return content.length > 0 ? new String(content, StandardCharsets.UTF_8) : "";
    }
}
