package me.loki2302.core.models;

import me.loki2302.core.Documented;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.group.GroupSequenceProvider;

import java.util.List;

@GroupSequenceProvider(MethodModelGroupSequenceProvider.class)
public class MethodModel {
    public String fullName;
    public String name;
    public List<ParameterModel> parameters;
    public String returnTypeName;

    @NotEmpty(groups = Documented.class, message = "Missing a method return value description. Please add a @return Javadoc tag.")
    public String returnDescription;

    public boolean isDocumented;

    @NotEmpty(groups = Documented.class, message = "Missing a method description. Please add a Javadoc comment.")
    public String description;

    public List<String> errors;
}
