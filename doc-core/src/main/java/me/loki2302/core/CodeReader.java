package me.loki2302.core;

import com.thoughtworks.qdox.model.*;
import me.loki2302.core.models.ClassModel;
import me.loki2302.core.models.FieldModel;
import me.loki2302.core.models.MethodModel;
import me.loki2302.core.models.ParameterModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Validator;
import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

public class CodeReader {
    private final static Logger LOGGER = LoggerFactory.getLogger(CodeReader.class);

    private final static String UNDOCUMENTED_TAG = "undocumented";
    private final static String STEREOTYPE_TAG = "stereotype";

    private final Validator validator;

    public CodeReader(Validator validator) {
        this.validator = validator;
    }

    public ClassModel readClass(JavaClass javaClass) {
        LOGGER.info("Reading class {}", javaClass.getFullyQualifiedName());

        ClassModel classModel = new ClassModel();
        classModel.fullName = javaClass.getFullyQualifiedName();
        classModel.name = javaClass.getName();
        classModel.source = getJavaClassSource(javaClass);
        classModel.isDocumented = !hasTag(javaClass, UNDOCUMENTED_TAG);
        classModel.description = getComment(javaClass);
        classModel.stereotype = getTagValue(javaClass, STEREOTYPE_TAG);

        classModel.errors = validator.validate(classModel)
                .stream()
                .map(v -> String.format("%s: %s", v.getPropertyPath(), v.getMessage()))
                .collect(Collectors.toList());

        classModel.methods = javaClass.getMethods()
                .stream()
                .map(javaMethod -> readMethod(classModel.isDocumented, javaMethod))
                .collect(Collectors.toList());

        classModel.fields = javaClass.getFields()
                .stream()
                .map(javaField -> readField(classModel.isDocumented, javaField))
                .collect(Collectors.toList());

        LOGGER.info("Read class {} with {} methods, {} fields and source {} (classErrors={}, methodErrors={})",
                classModel.name, classModel.methods.size(), classModel.fields.size(), classModel.source,
                classModel.errors.size(),
                classModel.methods.stream().flatMap(m -> m.errors.stream()).collect(Collectors.toList()).size());

        return classModel;
    }

    private MethodModel readMethod(boolean isClassDocumented, JavaMethod javaMethod) {
        MethodModel methodModel = new MethodModel();
        methodModel.fullName = String.format("%s::%s()", javaMethod.getDeclaringClass().getFullyQualifiedName(), javaMethod.getName());
        methodModel.name = javaMethod.getName();
        methodModel.isDocumented = isClassDocumented && !hasTag(javaMethod, UNDOCUMENTED_TAG);
        methodModel.description = getComment(javaMethod);

        methodModel.parameters = javaMethod.getParameters()
                .stream()
                .map(javaParameter -> readParameter(methodModel.isDocumented, javaMethod, javaParameter))
                .collect(Collectors.toList());

        methodModel.errors = validator.validate(methodModel).stream()
                .map(v -> String.format("%s: %s", v.getPropertyPath(), v.getMessage()))
                .collect(Collectors.toList());

        return methodModel;
    }

    private ParameterModel readParameter(boolean isMethodDocumented, JavaMethod javaMethod, JavaParameter javaParameter) {
        ParameterModel parameterModel = new ParameterModel();
        parameterModel.name = javaParameter.getName();
        parameterModel.typeName = javaParameter.getType().getFullyQualifiedName();
        parameterModel.isDocumented = isMethodDocumented;
        parameterModel.description = readParameterDescription(javaMethod, javaParameter);

        parameterModel.errors = validator.validate(parameterModel).stream()
                .map(v -> String.format("%s: %s", v.getPropertyPath(), v.getMessage()))
                .collect(Collectors.toList());

        return parameterModel;
    }

    private FieldModel readField(boolean isClassDocumented, JavaField javaField) {
        FieldModel fieldModel = new FieldModel();
        fieldModel.fullName = String.format("%s::%s", javaField.getDeclaringClass().getFullyQualifiedName(), javaField.getName());
        fieldModel.name = javaField.getName();
        fieldModel.typeName = javaField.getType().getFullyQualifiedName();
        fieldModel.isDocumented = isClassDocumented && !hasTag(javaField, UNDOCUMENTED_TAG);
        fieldModel.description = getComment(javaField);

        fieldModel.errors = validator.validate(fieldModel).stream()
                .map(v -> String.format("%s: %s", v.getPropertyPath(), v.getMessage()))
                .collect(Collectors.toList());

        return fieldModel;
    }

    private static String readParameterDescription(JavaMethod javaMethod, JavaParameter javaParameter) {
        List<DocletTag> paramDocletTags = javaMethod.getTagsByName("param");
        return paramDocletTags.stream()
                .filter(t -> t.getParameters().size() > 0)
                .filter(t -> t.getParameters().get(0).equals(javaParameter.getName()))
                .map(t -> t.getParameters().subList(1, t.getParameters().size()))
                .map(p -> String.join(" ", p))
                .findFirst().orElse("");
    }

    private static boolean hasTag(JavaAnnotatedElement javaAnnotatedElement, String tagName) {
        return javaAnnotatedElement.getTagByName(tagName) != null;
    }

    private static String getTagValue(JavaAnnotatedElement javaAnnotatedElement, String tagName) {
        DocletTag docletTag = javaAnnotatedElement.getTagByName(tagName);
        if(docletTag == null) {
            return "";
        }

        String value = docletTag.getValue();

        return value;
    }

    private static String getComment(JavaAnnotatedElement javaAnnotatedElement) {
        String comment = javaAnnotatedElement.getComment();
        if(comment == null) {
            return "";
        }

        return comment;
    }

    private static File getJavaClassSource(JavaClass javaClass) {
        try {
            return new File(javaClass.getSource().getURL().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
