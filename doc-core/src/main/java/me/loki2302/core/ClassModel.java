package me.loki2302.core;

import java.io.File;

public class ClassModel {
    public final String name;
    public final String stereotype;
    public final String description;
    public final File source;

    public ClassModel(String name, String stereotype, String description, File source) {
        this.name = name;
        this.stereotype = stereotype;
        this.description = description;
        this.source = source;
    }
}
