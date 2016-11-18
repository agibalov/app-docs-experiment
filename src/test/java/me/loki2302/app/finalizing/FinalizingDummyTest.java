package me.loki2302.app.finalizing;

import me.loki2302.documentation.SnippetWriter;
import me.loki2302.documentation.snippets.TextSnippet;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class FinalizingDummyTest {
    @Rule
    public final SnippetWriter snippetWriter = new SnippetWriter(System.getProperty("snippetsDir"));

    @Test
    public void finalizeDummy() throws IOException {
        Path dummyContentPath = Paths.get(System.getProperty("snippetsDir"), "DummyTest", "dummy", "dummy.txt");
        String dummyContent = new String(Files.readAllBytes(dummyContentPath));
        String finalizedContent = String.format("I am finalizing test %s:\n***\n%s\n", new Date(), dummyContent);
        snippetWriter.write("finalDummy.txt", new TextSnippet(finalizedContent));
    }
}
