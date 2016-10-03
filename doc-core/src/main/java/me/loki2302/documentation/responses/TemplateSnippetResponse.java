package me.loki2302.documentation.responses;

import me.loki2302.documentation.SnippetResponse;

public class TemplateSnippetResponse implements SnippetResponse {
    public final Object model;
    public final String templateName;

    public TemplateSnippetResponse(Object model, String templateName) {
        this.model = model;
        this.templateName = templateName;
    }
}
