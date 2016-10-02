package me.loki2302;

import me.loki2302.core.*;
import me.loki2302.core.snippets.JavaClassesSnippet;
import me.loki2302.core.snippets.ListSnippet;
import me.loki2302.core.snippets.StaticTextSnippet;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Test
    public void documentClasses() {
        CodebaseModel codebaseModel = CodebaseModelBuilder.buildCodebaseModel(new File("./src/main/java"));

        List<Map<String, String>> controllerClassModels =
                makeClassModelMaps(codebaseModel.findAllClassesByStereotype("controller"));
        snippetWriter.write("controllers.adoc", new JavaClassesSnippet(controllerClassModels));

        List<Map<String, String>> serviceClassModels =
                makeClassModelMaps(codebaseModel.findAllClassesByStereotype("service"));
        snippetWriter.write("services.adoc", new JavaClassesSnippet(serviceClassModels));

        List<Map<String, String>> repositoryClassModels =
                makeClassModelMaps(codebaseModel.findAllClassesByStereotype("repository"));
        snippetWriter.write("repositories.adoc", new JavaClassesSnippet(repositoryClassModels));
    }

    private static List<Map<String, String>> makeClassModelMaps(List<ClassModel> classModels) {
        return classModels.stream()
                .map(DocTest::makeClassModelMap)
                .collect(Collectors.toList());
    }

    private static Map<String, String> makeClassModelMap(ClassModel classModel) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("name", classModel.name);
        attributes.put("description", classModel.description);
        return attributes;
    }
}
