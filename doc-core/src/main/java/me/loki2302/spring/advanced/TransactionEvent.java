package me.loki2302.spring.advanced;

import lombok.ToString;

@ToString
public class TransactionEvent {
    public String tag;
    public TransactionEventType eventType;
    public String className;
    public String methodName;
}
