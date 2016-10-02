package me.loki2302.core;

public class SuccessfulModellingResult implements ModellingResult {
    public final ClassModel classModel;

    public SuccessfulModellingResult(ClassModel classModel) {
        this.classModel = classModel;
    }
}
