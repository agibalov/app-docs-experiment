package me.loki2302.core.snippets;

import me.loki2302.core.Snippet;
import me.loki2302.core.SnippetResponse;
import me.loki2302.core.responses.TemplateSnippetResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaClassesSnippet implements Snippet {
    private final List<Map<String, String>> javaClasses;

    public JavaClassesSnippet(List<Map<String, String>> javaClasses) {
        this.javaClasses = javaClasses;
    }

    @Override
    public SnippetResponse render() throws IOException {
        Map<String, Object> model = new HashMap<>();
        model.put("javaClasses", javaClasses);
        return new TemplateSnippetResponse(model, "java-classes-snippet.adoc");
    }
}
