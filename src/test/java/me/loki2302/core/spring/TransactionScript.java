package me.loki2302.core.spring;

import java.util.List;

public class TransactionScript {
    public final String title;
    public final List<String> steps;

    public TransactionScript(String title, List<String> steps) {
        this.title = title;
        this.steps = steps;
    }
}
