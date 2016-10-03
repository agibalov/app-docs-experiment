package me.loki2302.documentation;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import me.loki2302.documentation.responses.PlainTextSnippetResponse;
import me.loki2302.documentation.responses.TemplateSnippetResponse;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class SnippetWriter implements TestRule {
    private final String outputDirectory;
    private SnippetContext snippetContext;

    public SnippetWriter(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Path snippetsDirectoryPath = Paths.get(outputDirectory, description.getTestClass().getSimpleName(), description.getMethodName());
                snippetsDirectoryPath = Files.createDirectories(snippetsDirectoryPath);
                snippetContext = new SnippetContext(snippetsDirectoryPath);
                try {
                    base.evaluate();
                } finally {
                    snippetContext = null;
                }
            }
        };
    }

    public void write(String name, Snippet snippet) {
        try {
            snippetContext.renderSnippet(name, snippet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class SnippetContext {
        private final Path snippetsDirectoryPath;

        private SnippetContext(Path snippetsDirectoryPath) {
            this.snippetsDirectoryPath = snippetsDirectoryPath;
        }

        public void renderSnippet(String name, Snippet snippet) throws IOException {
            SnippetResponse snippetResponse = snippet.render();

            String content;
            if(snippetResponse instanceof PlainTextSnippetResponse) {
                PlainTextSnippetResponse plainTextSnippetResponse = (PlainTextSnippetResponse)snippetResponse;
                content = plainTextSnippetResponse.content;
            } else if(snippetResponse instanceof TemplateSnippetResponse) {
                TemplateSnippetResponse templateSnippetResponse = (TemplateSnippetResponse)snippetResponse;
                String templateName = templateSnippetResponse.templateName;
                Object model = templateSnippetResponse.model;

                Handlebars handlebars = new Handlebars();
                String templateString = Resources.toString(Resources.getResource(templateName), Charsets.UTF_8);
                Template template = handlebars.compileInline(templateString);
                content = template.apply(model);
            } else {
                throw new RuntimeException("Don't know how to handle " +
                        SnippetResponse.class.getSimpleName() +
                        " of type " +
                        snippetResponse.getClass().getName());
            }

            Path path = snippetsDirectoryPath.resolve(name);
            Files.write(path, Collections.singleton(content));
        }
    }
}
