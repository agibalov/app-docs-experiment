package me.loki2302.spring;

import me.loki2302.documentation.Snippet;
import me.loki2302.documentation.SnippetResponse;
import me.loki2302.documentation.responses.EjsSnippetResponse;

import java.io.IOException;

public class SequenceDiagramSnippet implements Snippet {
    private final TransactionFrame rootTransactionFrame;

    public SequenceDiagramSnippet(TransactionFrame rootTransactionFrame) {
        this.rootTransactionFrame = rootTransactionFrame;
    }

    @Override
    public SnippetResponse render() throws IOException {
        return new EjsSnippetResponse(rootTransactionFrame, "sequence-diagram-snippet.ejs");
    }
}
