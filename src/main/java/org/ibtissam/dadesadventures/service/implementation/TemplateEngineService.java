package org.ibtissam.dadesadventures.service.implementation;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class TemplateEngineService {
    private final TemplateEngine templateEngine;

    public TemplateEngineService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String processTemplate(String templateName, Context context) {
        return templateEngine.process(templateName, context);
    }
}
