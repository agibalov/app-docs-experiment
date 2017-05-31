package me.loki2302.documentation;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import me.loki2302.documentation.responses.EjsSnippetResponse;
import me.loki2302.documentation.responses.FileSnippetResponse;
import me.loki2302.documentation.responses.PlainTextSnippetResponse;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
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
        private final static Logger LOGGER = LoggerFactory.getLogger(SnippetContext.class);

        private final Path snippetsDirectoryPath;

        private SnippetContext(Path snippetsDirectoryPath) {
            this.snippetsDirectoryPath = snippetsDirectoryPath;
        }

        public void renderSnippet(String name, Snippet snippet) throws IOException {
            SnippetResponse snippetResponse = snippet.render();

            Path path = snippetsDirectoryPath.resolve(name);
            if(snippetResponse instanceof FileSnippetResponse) {
                FileSnippetResponse fileSnippetResponse = (FileSnippetResponse)snippetResponse;
                File sourceFile = fileSnippetResponse.sourceFile;
                Files.copy(sourceFile.toPath(), path);
            } else if(snippetResponse instanceof PlainTextSnippetResponse) {
                PlainTextSnippetResponse plainTextSnippetResponse = (PlainTextSnippetResponse)snippetResponse;
                String content = plainTextSnippetResponse.content;
                Files.write(path, Collections.singleton(content));
            } else if(snippetResponse instanceof EjsSnippetResponse) {
                EjsSnippetResponse ejsSnippetResponse = (EjsSnippetResponse)snippetResponse;
                String templateName = ejsSnippetResponse.templateName;
                Object model = ejsSnippetResponse.model;
                LOGGER.info("templateName={}, model={}", templateName, model);

                String templateString = Resources.toString(Resources.getResource(templateName), Charsets.UTF_8);

                ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
                ScriptEngine engine = scriptEngineManager.getEngineByName("nashorn");
                engine.put("template", templateString);
                engine.put("model", model);

                String content;
                try {
                    engine.eval("var modelProxy = Object.bindProperties({}, model)");
                    engine.eval("window = {}");
                    engine.eval("load('classpath:" +
                            "META-INF/resources/webjars/ejs/2.4.1/ejs-v2.4.1/ejs.js')");
                    content = (String)engine.eval("window.ejs.render(template, { model: modelProxy })");
                } catch (ScriptException e) {
                    throw new RuntimeException(e);
                }

                Files.write(path, Collections.singleton(content));
            } else {
                throw new RuntimeException("Don't know how to handle " +
                        SnippetResponse.class.getSimpleName() +
                        " of type " +
                        snippetResponse.getClass().getName());
            }
        }
    }
}
