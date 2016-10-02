package me.loki2302.core;

public class ErrorClassModel implements ClassModel {
    public final String message;

    public ErrorClassModel(String message) {
        this.message = message;
    }
}
