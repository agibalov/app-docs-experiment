package me.loki2302.core.spring;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TransactionScriptGenerator {
    @Autowired
    private TransactionTraceAspect transactionTraceAspect;

    public TransactionScript generateTransactionScript() {
        TxInfo txInfo = transactionTraceAspect.getLastTxInfo();
        List<String> steps = traverse(txInfo);

        TransactionScript transactionScript = new TransactionScript(txInfo.comment, steps);
        return transactionScript;
    }

    private static List<String> traverse(TxInfo txInfo) {
        List<String> steps = new ArrayList<>();

        if(txInfo.components.isEmpty()) {
            return steps;
        }

        for(TxInfo child : txInfo.components) {
            List<String> childSteps = traverse(child);

            steps.add(String.format("group %s", child.comment));
            steps.add(String.format("%s -> %s : %s::%s", txInfo.className, child.className, child.className, child.methodName));
            steps.addAll(childSteps);
            steps.add(String.format("%s <-- %s : %s::%s", txInfo.className, child.className, child.className, child.methodName));
            steps.add(String.format("end"));
        }

        return steps;
    }
}
