package me.loki2302.core;

public class SuccessClassModel implements ClassModel {
    public final String name;
    public final String stereotype;
    public final String description;

    public SuccessClassModel(String name, String stereotype, String description) {
        this.name = name;
        this.stereotype = stereotype;
        this.description = description;
    }
}
