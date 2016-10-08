package me.loki2302;

import me.loki2302.core.CodeReader;
import me.loki2302.core.CodebaseModel;
import me.loki2302.core.CodebaseModelGraphFacade;
import me.loki2302.core.models.ClassModel;
import me.loki2302.documentation.SnippetWriter;
import me.loki2302.documentation.snippets.ClassDiagramSnippet;
import me.loki2302.documentation.snippets.JavaClassesSnippet;
import org.junit.Rule;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DocTest {
    @Rule
    public final SnippetWriter snippetWriter = new SnippetWriter(System.getProperty("snippetsDir"));

    @Test
    public void documentClasses() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        CodeReader codeReader = new CodeReader(validator);
        CodebaseModelGraphFacade codebaseModelGraphFacade = new CodebaseModelGraphFacade();
        me.loki2302.core.CodebaseModelBuilder codebaseModelBuilder = new me.loki2302.core.CodebaseModelBuilder(
                codeReader,
                codebaseModelGraphFacade);
        CodebaseModel codebaseModel = codebaseModelBuilder.buildCodebaseModel(new File("./src/main/java"));

        List<Map<String, Object>> controllerClassModels =
                makeClassModelMaps(codebaseModel.findClassesByStereotype("controller"));
        snippetWriter.write("controllers.adoc", new JavaClassesSnippet(controllerClassModels));

        List<Map<String, Object>> serviceClassModels =
                makeClassModelMaps(codebaseModel.findClassesByStereotype("service"));
        snippetWriter.write("services.adoc", new JavaClassesSnippet(serviceClassModels));

        List<Map<String, Object>> repositoryClassModels =
                makeClassModelMaps(codebaseModel.findClassesByStereotype("repository"));
        snippetWriter.write("repositories.adoc", new JavaClassesSnippet(repositoryClassModels));

        List<Map<String, Object>> allClassModels =
                makeClassModelMaps(codebaseModel.findAllClasses());
        snippetWriter.write("classDiagram.puml", new ClassDiagramSnippet(allClassModels));
    }

    private static List<Map<String, Object>> makeClassModelMaps(List<ClassModel> classModels) {
        return classModels.stream()
                .map(DocTest::makeClassModelMap)
                .collect(Collectors.toList());
    }

    private static Map<String, Object> makeClassModelMap(ClassModel classModel) {
        List<Map<String, Object>> fields = classModel.fields.stream().map(f -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("name", f.name);
            attributes.put("type", f.typeName);
            attributes.put("description", f.description);
            return attributes;
        }).collect(Collectors.toList());

        List<Map<String, Object>> methods = classModel.methods.stream().map(m -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("name", m.name);
            attributes.put("description", m.description);
            return attributes;
        }).collect(Collectors.toList());

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("fullName", classModel.fullName);
        attributes.put("name", classModel.name);
        attributes.put("description", classModel.description);
        attributes.put("stereotype", classModel.stereotype);
        attributes.put("source", classModel.source.getName());
        attributes.put("fields", fields);
        attributes.put("methods", methods);
        return attributes;
    }
}
