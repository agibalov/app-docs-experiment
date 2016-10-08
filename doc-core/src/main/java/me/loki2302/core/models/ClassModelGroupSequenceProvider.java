package me.loki2302.core.models;

import me.loki2302.core.Documented;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

public class ClassModelGroupSequenceProvider implements DefaultGroupSequenceProvider<ClassModel> {
    @Override
    public List<Class<?>> getValidationGroups(ClassModel object) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(ClassModel.class);

        if(object != null) {
            if(object.isDocumented) {
                groups.add(Documented.class);
            }
        }

        return groups;
    }
}
