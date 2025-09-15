package com.recipe.web.core.thymeleaf;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.Set;

@Component
public class SecurityDialect extends AbstractProcessorDialect {

    public SecurityDialect() {
        super("Security Dialect", "sec", 1000);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return Set.of(new AuthorizationElementProcessor(dialectPrefix));
    }
}
