package me.loki2302.spring;

import lombok.ToString;

@ToString
public class TransactionEvent {
    public String tag;
    public String comment;
    public TransactionEventType eventType;
    public String className;
    public String methodName;
}
