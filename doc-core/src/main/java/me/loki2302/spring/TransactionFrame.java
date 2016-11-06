package me.loki2302.spring;

import java.util.ArrayList;
import java.util.List;

public class TransactionFrame {
    public final String tag;
    public final String comment;
    public final String className;
    public final String methodName;
    public final List<TransactionFrame> children = new ArrayList<>();

    public TransactionFrame(String tag, String comment, String className, String methodName) {
        this.tag = tag;
        this.comment = comment;
        this.className = className;
        this.methodName = methodName;
    }
}
