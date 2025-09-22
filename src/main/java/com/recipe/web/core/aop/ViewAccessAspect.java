package com.recipe.web.core.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ViewAccessAspect {

    @Around("execution(* com.recipe.web.app..controller.*Controller.*(..))")
    public Object checkViewAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        if (result instanceof String viewName) {
            String[] parts = viewName.split("/");
            String viewId = parts[parts.length - 1];

            if ("login".equals(viewId)) {
                return result;
            }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean allowed = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(viewId));

            if (!allowed) {
                throw new AccessDeniedException("Access denied to view: " + viewId);
            }
        }

        return result;
    }
}
