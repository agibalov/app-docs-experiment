package me.loki2302.core;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;

import java.util.Arrays;
import java.util.List;

public class ClassModelBuilder {
    public static ClassModel build(JavaClass javaClass) {
        List<DocletTag> undocumentedDocletTags = javaClass.getTagsByName("undocumented", true);
        if (!undocumentedDocletTags.isEmpty()) {
            return new SkipClassModel();
        }

        List<DocletTag> stereotypeDocletTags = javaClass.getTagsByName("stereotype", true);
        if (stereotypeDocletTags.isEmpty()) {
            return new ErrorClassModel(String.format("%s should either be marked with @undocumented, or have a @stereotype tag", javaClass.getName()));
        }

        if (stereotypeDocletTags.size() != 1) {
            throw new RuntimeException("More than one doclet tag of the same type. What do I do?");
        }

        DocletTag stereotypeDocletTag = stereotypeDocletTags.get(0);
        String stereotype = stereotypeDocletTag.getValue();
        if (!Arrays.asList("controller", "service", "repository").contains(stereotype)) {
            return new ErrorClassModel(String.format("%s has an unknown @stereotype value %s", javaClass.getName(), stereotype));
        }

        List<DocletTag> descriptionDocletTags = javaClass.getTagsByName("description", true);
        if(descriptionDocletTags.isEmpty()) {
            return new ErrorClassModel(String.format("%s should have a @description", javaClass.getName()));
        }

        if(descriptionDocletTags.size() != 1) {
            throw new RuntimeException("More than one doclet tag of the same type. What do I do?");
        }

        DocletTag descriptionDocletTag = descriptionDocletTags.get(0);
        String description = descriptionDocletTag.getValue();

        return new SuccessClassModel(javaClass.getName(), stereotype, description);
    }
}
