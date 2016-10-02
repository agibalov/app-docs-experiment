package me.loki2302.core;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.apache.tinkerpop.gremlin.util.iterator.IteratorUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CodebaseModelBuilder {
    public static CodebaseModel buildCodebaseModel(File sourceRoot) {
        JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
        javaProjectBuilder.addSourceTree(sourceRoot);

        List<ClassModel> classModels = javaProjectBuilder.getClasses()
                .stream()
                .map(javaClass -> modelClass(javaClass))
                .filter(modellingResult -> {
                    if(modellingResult instanceof SkipModellingResult) {
                        return false;
                    }

                    if(modellingResult instanceof ErrorModellingResult) {
                        ErrorModellingResult errorClassModel = (ErrorModellingResult) modellingResult;
                        throw new RuntimeException(errorClassModel.message);
                    }

                    if(modellingResult instanceof SuccessfulModellingResult) {
                        return true;
                    }

                    throw new RuntimeException();
                })
                .map(modellingResult -> ((SuccessfulModellingResult) modellingResult).classModel)
                .collect(Collectors.toList());

        Graph graph = TinkerGraph.open();

        for(ClassModel classModel : classModels) {
            graph.addVertex(
                    "fullName", classModel.fullName,
                    "name", classModel.name,
                    "description", makeEmptyIfNull(classModel.description),
                    "stereotype", makeEmptyIfNull(classModel.stereotype),
                    "source", classModel.source);
        }

        for(ClassModel classModel : classModels) {
            Vertex classModelVertex = IteratorUtils.list(graph.traversal().V().has("fullName", classModel.fullName)).get(0);

            classModel.fields.forEach(f -> {
                Vertex fieldVertex = graph.addVertex(
                    "fullName", f.fullName,
                    "name", f.name,
                    "type", f.type,
                    "description", makeEmptyIfNull(f.description));

                classModelVertex.addEdge("has-field", fieldVertex);
                // TODO: link field type to real type
            });

            classModel.methods.forEach(m -> {
                Vertex methodVertex = graph.addVertex(
                    "fullName", m.fullName,
                    "name", m.name,
                    "description", makeEmptyIfNull(m.description));

                classModelVertex.addEdge("has-method", methodVertex);
                // TODO: link return type to real type
                // TODO: link param types to real types
            });
        }

        return new CodebaseModel(graph);
    }

    public static ModellingResult modelClass(JavaClass javaClass) {
        List<DocletTag> undocumentedDocletTags = javaClass.getTagsByName("undocumented", true);
        if (!undocumentedDocletTags.isEmpty()) {
            return new SkipModellingResult();
        }

        List<DocletTag> stereotypeDocletTags = javaClass.getTagsByName("stereotype", true);
        if (stereotypeDocletTags.isEmpty()) {
            return new ErrorModellingResult(String.format("%s should either be marked with @undocumented, or have a @stereotype tag", javaClass.getName()));
        }

        if (stereotypeDocletTags.size() != 1) {
            throw new RuntimeException("More than one doclet tag of the same type. What do I do?");
        }

        DocletTag stereotypeDocletTag = stereotypeDocletTags.get(0);
        String stereotype = stereotypeDocletTag.getValue();
        if (!Arrays.asList("controller", "service", "repository").contains(stereotype)) {
            return new ErrorModellingResult(String.format("%s has an unknown @stereotype value %s", javaClass.getName(), stereotype));
        }

        List<DocletTag> descriptionDocletTags = javaClass.getTagsByName("description", true);
        if(descriptionDocletTags.isEmpty()) {
            return new ErrorModellingResult(String.format("%s should have a @description", javaClass.getName()));
        }

        if(descriptionDocletTags.size() != 1) {
            throw new RuntimeException("More than one doclet tag of the same type. What do I do?");
        }

        DocletTag descriptionDocletTag = descriptionDocletTags.get(0);
        String description = descriptionDocletTag.getValue();

        File source;
        try {
            source = new File(javaClass.getSource().getURL().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        List<JavaField> javaFields = javaClass.getFields();
        List<FieldModel> fieldModels = javaFields.stream()
                .map(CodebaseModelBuilder::modelField)
                .collect(Collectors.toList());

        List<JavaMethod> javaMethods = javaClass.getMethods(true);
        List<MethodModel> methodModels = javaMethods.stream()
                .filter(m -> !m.getDeclaringClass().getFullyQualifiedName().equals(Object.class.getName()))
                .map(CodebaseModelBuilder::modelMethod)
                .collect(Collectors.toList());

        ClassModel classModel = new ClassModel(
                javaClass.getFullyQualifiedName(),
                javaClass.getName(),
                stereotype,
                description,
                source,
                fieldModels,
                methodModels);

        return new SuccessfulModellingResult(classModel);
    }

    private static FieldModel modelField(JavaField javaField) {
        FieldModel fieldModel = new FieldModel(
                String.format("%s::%s", javaField.getDeclaringClass().getFullyQualifiedName(), javaField.getName()),
                javaField.getName(),
                javaField.getType().getFullyQualifiedName(),
                javaField.getComment());

        return fieldModel;
    }

    private static MethodModel modelMethod(JavaMethod javaMethod) {
        MethodModel methodModel = new MethodModel(
                String.format("%s::%s()", javaMethod.getDeclaringClass().getFullyQualifiedName(), javaMethod.getName()),
                javaMethod.getName(),
                javaMethod.getComment());

        return methodModel;
    }

    private static String makeEmptyIfNull(String s) {
        return s != null ? s : "";
    }
}
