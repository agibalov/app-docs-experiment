package me.loki2302.core.spring;

import java.util.ArrayList;
import java.util.List;

public class TxInfo {
    public final String className;
    public final String methodName;
    public final String comment;
    public final List<TxInfo> components = new ArrayList<>();

    public TxInfo(String className, String methodName, String comment) {
        this.className = className;
        this.methodName = methodName;
        this.comment = comment;
    }
}
