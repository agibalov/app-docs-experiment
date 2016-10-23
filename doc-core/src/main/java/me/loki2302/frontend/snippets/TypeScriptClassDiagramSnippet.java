package me.loki2302.frontend.snippets;

import me.loki2302.documentation.Snippet;
import me.loki2302.documentation.SnippetResponse;
import me.loki2302.documentation.responses.EjsSnippetResponse;
import me.loki2302.frontend.TypeScriptCodeModel;

import java.io.IOException;

public class TypeScriptClassDiagramSnippet implements Snippet {
    private final TypeScriptCodeModel typeScriptCodeModel;

    public TypeScriptClassDiagramSnippet(TypeScriptCodeModel typeScriptCodeModel) {
        this.typeScriptCodeModel = typeScriptCodeModel;
    }

    @Override
    public SnippetResponse render() throws IOException {
        return new EjsSnippetResponse(typeScriptCodeModel, "ts-class-diagram-snippet.ejs");
    }
}
