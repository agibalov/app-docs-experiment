package me.loki2302.core;

public class FieldModel {
    public final String fullName;
    public final String name;
    public final String type;
    public final String description;

    public FieldModel(String fullName, String name, String type, String description) {
        this.fullName = fullName;
        this.name = name;
        this.type = type;
        this.description = description;
    }
}
