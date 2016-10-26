package me.loki2302.spring.advanced;

import me.loki2302.documentation.Snippet;
import me.loki2302.documentation.SnippetResponse;
import me.loki2302.documentation.responses.EjsSnippetResponse;

import java.io.IOException;

public class AdvancedSequenceDiagramSnippet implements Snippet {
    private final TransactionFrame rootTransactionFrame;

    public AdvancedSequenceDiagramSnippet(TransactionFrame rootTransactionFrame) {
        this.rootTransactionFrame = rootTransactionFrame;
    }

    @Override
    public SnippetResponse render() throws IOException {
        return new EjsSnippetResponse(rootTransactionFrame, "sequence-diagram-2-snippet.ejs");
    }
}
