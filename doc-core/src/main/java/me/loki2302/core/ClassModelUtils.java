package me.loki2302.core;

import com.thoughtworks.qdox.JavaProjectBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassModelUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClassModelUtils.class);

    public static List<Map<String, String>> buildClassModel(File sourceRoot, String stereotype) {
        JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
        javaProjectBuilder.addSourceTree(sourceRoot);

        List<Map<String, String>> javaClassModels = javaProjectBuilder.getClasses()
                .stream()
                .map(javaClass -> ClassModelBuilder.build(javaClass))
                .filter(classModel -> {
                    if(classModel instanceof SkipClassModel) {
                        return false;
                    }

                    if(classModel instanceof ErrorClassModel) {
                        ErrorClassModel errorClassModel = (ErrorClassModel)classModel;
                        throw new RuntimeException(errorClassModel.message);
                    }

                    if(classModel instanceof SuccessClassModel) {
                        SuccessClassModel successClassModel = (SuccessClassModel)classModel;
                        if(!successClassModel.stereotype.equals(stereotype)) {
                            return false;
                        }

                        return true;
                    }

                    throw new RuntimeException();
                })
                .map(classModel -> (SuccessClassModel)classModel)
                .map(classModel -> {
                    Map<String, String> attributes = new HashMap<>();
                    attributes.put("name", classModel.name);
                    attributes.put("description", classModel.description);
                    return attributes;
                })
                .collect(Collectors.toList());

        return javaClassModels;
    }
}
