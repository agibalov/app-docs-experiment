package me.loki2302.app;

import me.loki2302.documentation.SnippetWriter;
import me.loki2302.documentation.snippets.TextSnippet;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

public class DummyTest {
    @Rule
    public final SnippetWriter snippetWriter = new SnippetWriter(System.getProperty("snippetsDir"));

    @Test
    public void dummy() {
        String message = String.format("I am DummyTest! %s", new Date());
        snippetWriter.write("dummy.txt", new TextSnippet(message));
    }
}
