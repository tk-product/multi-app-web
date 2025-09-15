package com.recipe.web.core.thymeleaf;

import com.recipe.web.app.common.session.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class AuthorizationElementProcessor extends AbstractElementTagProcessor {

    private static final String TAG_NAME = "authorize";
    private static final int PRECEDENCE = 1000;

    public AuthorizationElementProcessor(String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, PRECEDENCE);
    }

    @Override
    protected void doProcess(ITemplateContext context,
                             IProcessableElementTag tag,
                             IElementTagStructureHandler structureHandler) {

        String requiredRole = tag.getAttributeValue("role");
        String requiredPermission = tag.getAttributeValue("permission");

        HttpServletRequest request = null;

        // 安全に HttpServletRequest を取得
        Object requestVar = context.getVariable("request");
        if (requestVar instanceof HttpServletRequest) {
            request = (HttpServletRequest) requestVar;
        }

        boolean allowed = false;
        if (request != null) {
            UserSession user = (UserSession) request.getSession().getAttribute("USER_SESSION");
            if (user != null) {
                boolean roleMatch = requiredRole == null || requiredRole.equalsIgnoreCase(user.getRole());
                boolean permMatch = requiredPermission == null || user.getPermissionLevel().contains(requiredPermission);
                allowed = roleMatch && permMatch;
            }
        }

        if (!allowed) {
            structureHandler.removeElement();
        }
    }
}
