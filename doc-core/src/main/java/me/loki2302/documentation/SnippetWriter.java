package me.loki2302.documentation;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import me.loki2302.documentation.responses.EjsSnippetResponse;
import me.loki2302.documentation.responses.PlainTextSnippetResponse;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

            String content;
            if(snippetResponse instanceof PlainTextSnippetResponse) {
                PlainTextSnippetResponse plainTextSnippetResponse = (PlainTextSnippetResponse)snippetResponse;
                content = plainTextSnippetResponse.content;
            } else if(snippetResponse instanceof EjsSnippetResponse) {
                EjsSnippetResponse ejsSnippetResponse = (EjsSnippetResponse)snippetResponse;
                String templateName = ejsSnippetResponse.templateName;
                Object model = ejsSnippetResponse.model;
                LOGGER.info("templateName={}, model={}", templateName, model);

                String templateString = Resources.toString(Resources.getResource(templateName), Charsets.UTF_8);
                String ejsString = Resources.toString(
                        Resources.getResource("META-INF/resources/webjars/ejs/2.4.1/ejs-v2.4.1/ejs.js"),
                        Charsets.UTF_8);
                Context context = Context.enter();
                try {
                    Scriptable scope = context.initStandardObjects();

                    ScriptableObject.putProperty(scope, "template", templateString);

                    Object modelObj = Context.javaToJS(model, scope);
                    ScriptableObject.putProperty(scope, "model", modelObj);

                    context.evaluateString(scope, "window = {}", "browser.js", 1, null);
                    context.evaluateString(scope, ejsString, "ejs.js", 1, null);

                    content = (String)context.evaluateString(
                            scope,
                            "window.ejs.render(template, { model: model })",
                            "renderer.js",
                            1,
                            null);
                } finally {
                    context.exit();
                }
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
