package me.loki2302.core;

import java.io.File;
import java.util.List;

public class ClassModel {
    public final String fullName;
    public final String name;
    public final String stereotype;
    public final String description;
    public final File source;
    public final List<FieldModel> fields;
    public final List<MethodModel> methods;

    public ClassModel(
            String fullName,
            String name,
            String stereotype,
            String description,
            File source,
            List<FieldModel> fields, List<MethodModel> methods) {

        this.fullName = fullName;
        this.name = name;
        this.stereotype = stereotype;
        this.description = description;
        this.source = source;
        this.fields = fields;
        this.methods = methods;
    }
}
