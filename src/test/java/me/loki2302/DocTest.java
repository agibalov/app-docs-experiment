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
                .filter(classModel -> classModel.isDocumented)
                .map(DocTest::makeClassModelMap)
                .collect(Collectors.toList());
    }

    private static Map<String, Object> makeClassModelMap(ClassModel classModel) {
        List<Map<String, Object>> fields = classModel.fields.stream()
                .filter(fieldModel -> fieldModel.isDocumented)
                .map(fieldModel -> {
                    Map<String, Object> fieldAttributes = new HashMap<>();
                    fieldAttributes.put("name", fieldModel.name);
                    fieldAttributes.put("type", fieldModel.typeName);
                    fieldAttributes.put("description", fieldModel.description);
                    return fieldAttributes;
                }).collect(Collectors.toList());

        List<Map<String, Object>> methods = classModel.methods.stream()
                .filter(methodModel -> methodModel.isDocumented)
                .map(methodModel -> {
                    Map<String, Object> methodAttributes = new HashMap<>();
                    methodAttributes.put("name", methodModel.name);
                    methodAttributes.put("description", methodModel.description);

                    List<Map<String, Object>> methodParameterAttributesList = methodModel.parameters.stream()
                            .filter(parameterModel -> parameterModel.isDocumented)
                            .map(parameterModel -> {
                                Map<String, Object> parameterAttributes = new HashMap<>();
                                parameterAttributes.put("name", parameterModel.name);
                                parameterAttributes.put("typeName", parameterModel.typeName);
                                parameterAttributes.put("description", parameterModel.description);
                                return parameterAttributes;
                            }).collect(Collectors.toList());
                    methodAttributes.put("parameters", methodParameterAttributesList);

                    return methodAttributes;
                }).collect(Collectors.toList());

        Map<String, Object> classAttributes = new HashMap<>();
        classAttributes.put("fullName", classModel.fullName);
        classAttributes.put("name", classModel.name);
        classAttributes.put("description", classModel.description);
        classAttributes.put("stereotype", classModel.stereotype);
        classAttributes.put("source", classModel.source.getName());
        classAttributes.put("fields", fields);
        classAttributes.put("methods", methods);
        return classAttributes;
    }
}
