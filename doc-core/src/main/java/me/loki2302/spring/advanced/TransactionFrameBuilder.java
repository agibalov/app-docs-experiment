package me.loki2302.spring.advanced;

import java.util.Stack;

public class TransactionFrameBuilder {
    private Transaction transaction;

    public void handleTransactionEvent(TransactionEvent transactionEvent) {
        if(transactionEvent.eventType.equals(TransactionEventType.Enter)) {
            enter(transactionEvent);
        } else if(transactionEvent.eventType.equals(TransactionEventType.Leave)) {
            leave();
        } else {
            throw new RuntimeException();
        }
    }

    private void enter(TransactionEvent transactionEvent) {
        TransactionFrame transactionFrame = new TransactionFrame(
                transactionEvent.tag,
                transactionEvent.className,
                transactionEvent.methodName);

        if(transaction == null) {
            transaction = new Transaction(transactionFrame);
        } else {
            transaction.frameStack.peek().children.add(transactionFrame);
            transaction.frameStack.push(transactionFrame);
        }
    }

    private void leave() {
        transaction.frameStack.pop();
    }

    public TransactionFrame getRootTransactionFrame() {
        if(!transaction.frameStack.isEmpty()) {
            throw new IllegalStateException();
        }

        return transaction.rootFrame;
    }

    private static class Transaction {
        public final TransactionFrame rootFrame;
        public final Stack<TransactionFrame> frameStack = new Stack<>();

        public Transaction(TransactionFrame rootFrame) {
            this.rootFrame = rootFrame;
            frameStack.push(rootFrame);
        }
    }
}
