package me.loki2302.core;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;

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

        return new CodebaseModel(classModels);
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
        ClassModel classModel = new ClassModel(javaClass.getName(), stereotype, description, source);

        return new SuccessfulModellingResult(classModel);
    }
}
