package me.loki2302.core;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class CodebaseModel {
    private final List<ClassModel> classModels;

    public CodebaseModel(List<ClassModel> classModels) {
        this.classModels = classModels;
    }

    public List<ClassModel> findClassesByFile(File source) {
        return classModels.stream()
                .filter(classModel -> classModel.source.equals(source))
                .collect(Collectors.toList());
    }

    public List<ClassModel> findClassesByStereotype(String stereotype) {
        return classModels.stream()
                .filter(classModel -> classModel.stereotype.equals(stereotype))
                .collect(Collectors.toList());
    }
}
