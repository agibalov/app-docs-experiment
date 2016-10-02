package me.loki2302.core.responses;

import me.loki2302.core.SnippetResponse;

public class TemplateSnippetResponse implements SnippetResponse {
    public final Object model;
    public final String templateName;

    public TemplateSnippetResponse(Object model, String templateName) {
        this.model = model;
        this.templateName = templateName;
    }
}
