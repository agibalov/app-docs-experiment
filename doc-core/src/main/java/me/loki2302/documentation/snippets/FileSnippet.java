package me.loki2302.documentation.snippets;

import me.loki2302.documentation.Snippet;
import me.loki2302.documentation.SnippetResponse;
import me.loki2302.documentation.responses.FileSnippetResponse;

import java.io.File;
import java.io.IOException;

public class FileSnippet implements Snippet {
    private final File sourceFile;

    public FileSnippet(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    @Override
    public SnippetResponse render() throws IOException {
        return new FileSnippetResponse(sourceFile);
    }
}
