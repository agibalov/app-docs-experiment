package me.loki2302.spring.advanced;

import java.util.ArrayList;
import java.util.List;

public class TransactionFrame {
    public final String tag;
    public final String className;
    public final String methodName;
    public final List<TransactionFrame> children = new ArrayList<>();

    public TransactionFrame(String tag, String className, String methodName) {
        this.tag = tag;
        this.className = className;
        this.methodName = methodName;
    }
}
