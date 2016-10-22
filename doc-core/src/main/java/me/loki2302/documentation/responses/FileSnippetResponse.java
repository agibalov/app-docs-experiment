package me.loki2302.documentation.responses;

import me.loki2302.documentation.SnippetResponse;

import java.io.File;

public class FileSnippetResponse implements SnippetResponse {
    public final File sourceFile;

    public FileSnippetResponse(File sourceFile) {
        this.sourceFile = sourceFile;
    }
}
