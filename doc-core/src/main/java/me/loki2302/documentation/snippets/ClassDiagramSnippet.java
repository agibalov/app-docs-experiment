package me.loki2302.documentation.snippets;

import me.loki2302.core.models.ClassModel;
import me.loki2302.documentation.Snippet;
import me.loki2302.documentation.SnippetResponse;
import me.loki2302.documentation.responses.EjsSnippetResponse;

import java.io.IOException;
import java.util.List;

public class ClassDiagramSnippet implements Snippet {
    private final List<ClassModel> classes;

    public ClassDiagramSnippet(List<ClassModel> classes) {
        this.classes = classes;
    }

    @Override
    public SnippetResponse render() throws IOException {
        return new EjsSnippetResponse(classes, "class-diagram-snippet.ejs");
    }
}
