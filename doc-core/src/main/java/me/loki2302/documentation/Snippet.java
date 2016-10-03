package me.loki2302.documentation;

import java.io.IOException;

public interface Snippet {
    SnippetResponse render() throws IOException;
}
