package me.loki2302.documentation.snippets;

import me.loki2302.documentation.Snippet;
import me.loki2302.documentation.SnippetResponse;
import me.loki2302.documentation.responses.EjsSnippetResponse;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SequenceDiagramSnippet implements Snippet {
    private final String title;
    private final List<String> links;

    public SequenceDiagramSnippet(String title, List<String> links) {
        this.title = title;
        this.links = links;
    }

    @Override
    public SnippetResponse render() throws IOException {
        SequenceDiagramSnippetModel sequenceDiagramSnippetModel = new SequenceDiagramSnippetModel();
        sequenceDiagramSnippetModel.title = title;
        sequenceDiagramSnippetModel.links = links;
        return new EjsSnippetResponse(sequenceDiagramSnippetModel, "sequence-diagram-snippet.ejs");
    }

    public static class SequenceDiagramSnippetModel {
        public String title;
        public List<String> links;
    }

}
