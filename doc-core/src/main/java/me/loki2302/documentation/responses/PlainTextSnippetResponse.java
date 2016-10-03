package me.loki2302.documentation.responses;

import me.loki2302.documentation.SnippetResponse;

public class PlainTextSnippetResponse implements SnippetResponse {
    public final String content;

    public PlainTextSnippetResponse(String content) {
        this.content = content;
    }
}
