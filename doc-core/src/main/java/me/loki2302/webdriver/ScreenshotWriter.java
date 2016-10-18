package me.loki2302.webdriver;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScreenshotWriter implements TestRule {
    private final String outputDirectory;
    private ScreenshotContext screenshotContext;

    public ScreenshotWriter(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Path snippetsDirectoryPath = Paths.get(outputDirectory, description.getTestClass().getSimpleName(), description.getMethodName());
                snippetsDirectoryPath = Files.createDirectories(snippetsDirectoryPath);
                screenshotContext = new ScreenshotContext(snippetsDirectoryPath);
                try {
                    base.evaluate();
                } finally {
                    screenshotContext = null;
                }
            }
        };
    }

    public void write(String name, File source) {
        try {
            screenshotContext.write(name, source);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class ScreenshotContext {
        private final Path snippetsDirectoryPath;

        private ScreenshotContext(Path snippetsDirectoryPath) {
            this.snippetsDirectoryPath = snippetsDirectoryPath;
        }

        public void write(String name, File source) throws IOException {
            Path path = snippetsDirectoryPath.resolve(name);
            Files.copy(source.toPath(), path);
        }
    }
}
