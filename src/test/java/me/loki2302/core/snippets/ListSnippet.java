package me.loki2302.core.snippets;

import me.loki2302.core.Snippet;
import me.loki2302.core.SnippetResponse;
import me.loki2302.core.responses.TemplateSnippetResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListSnippet implements Snippet {
    private final List<String> items;

    public ListSnippet(List<String> items) {
        this.items = items;
    }

    @Override
    public SnippetResponse render() throws IOException {
        Map<String, Object> model = new HashMap<>();
        model.put("items", items);
        return new TemplateSnippetResponse(model, "list-snippet.adoc");
    }
}
