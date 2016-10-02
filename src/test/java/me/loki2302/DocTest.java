package me.loki2302;

import me.loki2302.core.SnippetWriter;
import me.loki2302.core.snippets.ListSnippet;
import me.loki2302.core.snippets.StaticTextSnippet;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class DocTest {
    @Rule
    public final SnippetWriter snippetWriter = new SnippetWriter(System.getProperty("snippetsDir"));

    @Test
    public void documentText() {
        snippetWriter.write("hello.adoc", new StaticTextSnippet("hello world"));
    }

    @Test
    public void documentList() {
        List<String> items = Arrays.asList("Item One", "Item Two", "Item Three");
        snippetWriter.write("list.adoc", new ListSnippet(items));
    }
}
