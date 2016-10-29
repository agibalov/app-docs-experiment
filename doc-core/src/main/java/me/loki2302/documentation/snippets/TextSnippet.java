package me.loki2302.documentation.snippets;

import me.loki2302.documentation.Snippet;
import me.loki2302.documentation.SnippetResponse;
import me.loki2302.documentation.responses.PlainTextSnippetResponse;

import java.io.IOException;

public class TextSnippet implements Snippet {
    private final String text;

    public TextSnippet(String text) {
        this.text = text;
    }

    @Override
    public SnippetResponse render() throws IOException {
        return new PlainTextSnippetResponse(text);
    }
}
