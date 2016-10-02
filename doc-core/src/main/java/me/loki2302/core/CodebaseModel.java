package me.loki2302.core;

import java.util.List;
import java.util.stream.Collectors;

public class CodebaseModel {
    private final List<ClassModel> classModels;

    public CodebaseModel(List<ClassModel> classModels) {
        this.classModels = classModels;
    }

    public List<ClassModel> findAllClassesByStereotype(String stereotype) {
        return classModels.stream()
                .filter(classModel -> classModel.stereotype.equals(stereotype))
                .collect(Collectors.toList());
    }
}
