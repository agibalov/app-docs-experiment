package me.loki2302.core;

import java.io.IOException;

public interface Snippet {
    SnippetResponse render() throws IOException;
}
