package me.loki2302.core;

public class ErrorModellingResult implements ModellingResult {
    public final String message;

    public ErrorModellingResult(String message) {
        this.message = message;
    }
}
