package me.loki2302.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.loki2302.documentation.SnippetWriter;
import me.loki2302.frontend.TypeScriptCodeModel;
import me.loki2302.frontend.snippets.TypeScriptClassDiagramSnippet;
import me.loki2302.frontend.snippets.TypeScriptClassesSnippet;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class FrontEndDocTest {
    private final Logger LOGGER = LoggerFactory.getLogger(FrontEndDocTest.class);

    @Rule
    public final SnippetWriter snippetWriter = new SnippetWriter(System.getProperty("snippetsDir"));

    @Test
    public void documentClasses() throws IOException {
        String codeModelJsonString = StreamUtils.copyToString(
                this.getClass().getResourceAsStream("/resources/code-model.json"),
                Charset.forName("UTF-8"));
        LOGGER.info("Read as {}", codeModelJsonString);

        ObjectMapper objectMapper = new ObjectMapper();
        TypeScriptCodeModel typeScriptCodeModel = objectMapper.readValue(codeModelJsonString, TypeScriptCodeModel.class);
        LOGGER.info("Interpreted as {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(typeScriptCodeModel));

        snippetWriter.write("classes.adoc", new TypeScriptClassesSnippet(typeScriptCodeModel));
        snippetWriter.write("classDiagram.puml", new TypeScriptClassDiagramSnippet(typeScriptCodeModel));
    }
}
