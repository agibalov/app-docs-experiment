package me.loki2302.core.models;

import me.loki2302.core.Documented;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

public class FieldModelGroupSequenceProvider implements DefaultGroupSequenceProvider<FieldModel> {
    @Override
    public List<Class<?>> getValidationGroups(FieldModel object) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(FieldModel.class);

        if(object != null) {
            if(object.isDocumented) {
                groups.add(Documented.class);
            }
        }

        return groups;
    }
}
