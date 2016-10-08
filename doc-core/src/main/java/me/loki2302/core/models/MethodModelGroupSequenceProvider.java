package me.loki2302.core.models;

import me.loki2302.core.Documented;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

public class MethodModelGroupSequenceProvider implements DefaultGroupSequenceProvider<MethodModel> {
    @Override
    public List<Class<?>> getValidationGroups(MethodModel object) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(MethodModel.class);

        if(object != null) {
            if(object.isDocumented) {
                groups.add(Documented.class);
            }
        }

        return groups;
    }
}
