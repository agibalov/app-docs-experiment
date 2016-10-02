package me.loki2302.core.snippets;

import me.loki2302.core.responses.PlainTextSnippetResponse;
import me.loki2302.core.Snippet;
import me.loki2302.core.SnippetResponse;

import java.io.IOException;

public class StaticTextSnippet implements Snippet {
    private final String text;

    public StaticTextSnippet(String text) {
        this.text = text;
    }

    @Override
    public SnippetResponse render() throws IOException {
        return new PlainTextSnippetResponse(text);
    }
}
