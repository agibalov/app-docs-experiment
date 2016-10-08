package me.loki2302.core.models;

import me.loki2302.core.Documented;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.group.GroupSequenceProvider;

import java.io.File;
import java.util.List;

@GroupSequenceProvider(ClassModelGroupSequenceProvider.class)
public class ClassModel {
    public String fullName;
    public String name;
    public File source;

    public boolean isDocumented;

    @NotEmpty(groups = Documented.class)
    public String description;

    @NotEmpty(groups = Documented.class)
    public String stereotype;

    public List<MethodModel> methods;
    public List<FieldModel> fields;

    public List<String> errors;
}
