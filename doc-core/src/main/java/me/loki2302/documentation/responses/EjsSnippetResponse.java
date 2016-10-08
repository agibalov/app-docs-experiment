package me.loki2302.documentation.responses;

import me.loki2302.documentation.SnippetResponse;

public class EjsSnippetResponse implements SnippetResponse {
    public final Object model;
    public final String templateName;

    public EjsSnippetResponse(Object model, String templateName) {
        this.model = model;
        this.templateName = templateName;
    }
}
