package com.recipe.web.exception;

import com.recipe.web.app.common.form.ErrorForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private HttpServletRequest request;
    private HttpServletResponse response;

    @ExceptionHandler(Throwable.class)
    public String handleException(Throwable ex, Model model, HttpSession session) {
        System.out.println("★★★handleException : ");
        ErrorForm errorForm = new ErrorForm();

        // Exceptionのメッセージ
        errorForm.setErrorMessage(ex.getMessage());

        // StackTraceを文字列に変換
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        errorForm.setStackTrace(sw.toString());

        String sessionId = request.getSession().getId();
        int status = response.getStatus();
        String requestId = MDC.get("X-Request-Id");
        logger.info("SessionID: {}, RequestID: {}, Response Status: {}", sessionId, requestId, status);
        logger.error("SessionID: {}, RequestID: {}, StackTrace: {}", sessionId, requestId, errorForm.getStackTrace());

        // セッション情報を取得
        Map<String, Object> sessionMap = new HashMap<>();
        Enumeration<String> attrNames = session.getAttributeNames();
        while (attrNames.hasMoreElements()) {
            String name = attrNames.nextElement();
            sessionMap.put(name, session.getAttribute(name));
        }
        errorForm.setSessionAttributes(sessionMap);

        model.addAttribute("errorForm", errorForm);
        return "error"; // error.htmlに遷移
    }
}