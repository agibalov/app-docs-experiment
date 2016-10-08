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

    @NotEmpty(groups = Documented.class, message = "Missing a class description. Please add a Javadoc comment.")
    public String description;

    @NotEmpty(groups = Documented.class, message = "Missing a class stereotype. Please add a @stereotype (controller, service, repository) Javadoc tag.")
    public String stereotype;

    public List<MethodModel> methods;
    public List<FieldModel> fields;

    public List<String> errors;
}
