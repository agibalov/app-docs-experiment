package me.loki2302.core.responses;

import me.loki2302.core.SnippetResponse;

public class PlainTextSnippetResponse implements SnippetResponse {
    public final String content;

    public PlainTextSnippetResponse(String content) {
        this.content = content;
    }
}
