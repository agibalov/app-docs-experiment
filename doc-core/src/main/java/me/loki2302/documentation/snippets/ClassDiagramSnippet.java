package me.loki2302.documentation.snippets;

import me.loki2302.documentation.Snippet;
import me.loki2302.documentation.SnippetResponse;
import me.loki2302.documentation.responses.HandlebarsSnippetResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassDiagramSnippet implements Snippet {
    private final List<Map<String, Object>> javaClasses;

    public ClassDiagramSnippet(List<Map<String, Object>> javaClasses) {
        this.javaClasses = javaClasses;
    }

    @Override
    public SnippetResponse render() throws IOException {
        Map<String, Object> model = new HashMap<>();
        model.put("javaClasses", javaClasses);
        return new HandlebarsSnippetResponse(model, "class-diagram-snippet.puml");
    }
}
