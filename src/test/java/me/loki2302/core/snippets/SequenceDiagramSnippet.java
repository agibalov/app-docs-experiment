package me.loki2302.core.snippets;

import me.loki2302.core.Snippet;
import me.loki2302.core.SnippetResponse;
import me.loki2302.core.responses.TemplateSnippetResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SequenceDiagramSnippet implements Snippet {
    private final String title;
    private final List<String> links;

    public SequenceDiagramSnippet(String title, List<String> links) {
        this.title = title;
        this.links = links;
    }

    @Override
    public SnippetResponse render() throws IOException {
        Map<String, Object> model = new HashMap<>();
        model.put("title", title);
        model.put("links", links);
        return new TemplateSnippetResponse(model, "sequence-diagram-snippet.puml");
    }
}
