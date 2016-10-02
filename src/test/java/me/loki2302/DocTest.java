package me.loki2302;

import me.loki2302.core.ClassModelUtils;
import me.loki2302.core.SnippetWriter;
import me.loki2302.core.snippets.JavaClassesSnippet;
import me.loki2302.core.snippets.ListSnippet;
import me.loki2302.core.snippets.StaticTextSnippet;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DocTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(DocTest.class);

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

    @Test
    public void documentClasses() {
        List<Map<String, String>> controllerClassModels = ClassModelUtils.buildClassModel(new File("./src/main/java"), "controller");
        snippetWriter.write("controllers.adoc", new JavaClassesSnippet(controllerClassModels));

        List<Map<String, String>> serviceClassModels = ClassModelUtils.buildClassModel(new File("./src/main/java"), "service");
        snippetWriter.write("services.adoc", new JavaClassesSnippet(serviceClassModels));

        List<Map<String, String>> repositoryClassModels = ClassModelUtils.buildClassModel(new File("./src/main/java"), "repository");
        snippetWriter.write("repositories.adoc", new JavaClassesSnippet(repositoryClassModels));
    }
}
