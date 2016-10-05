package me.loki2302.spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Aspect
public class TransactionRecorder {
    private boolean isRecording;
    private TxInfo rootTxInfo;
    private Stack<TxInfo> txStack;

    public void startTransaction() {
        isRecording = true;
    }

    public void stopTransaction() {
        isRecording = false;
    }

    public TransactionScript getTransactionScript() {
        List<String> steps = traverse(rootTxInfo);
        TransactionScript transactionScript = new TransactionScript(rootTxInfo.comment, steps);
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

    @Around("execution(public * org.springframework.data.repository.Repository+.*(..))")
    public Object traceSpringRepositoryAccess(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Signature signature = proceedingJoinPoint.getSignature();
        if(signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature)signature;

            TransactionEntryPoint transactionEntryPoint = AnnotationUtils.getAnnotation(
                    methodSignature.getMethod(),
                    TransactionEntryPoint.class);
            if(transactionEntryPoint != null) {
                return handleTransaction(proceedingJoinPoint);
            }

            TransactionComponent transactionComponent = AnnotationUtils.getAnnotation(
                    methodSignature.getMethod(),
                    TransactionComponent.class);
            if(transactionComponent != null) {
                return handleTransactionComponent(proceedingJoinPoint);
            }
        }

        return proceedingJoinPoint.proceed();
    }

    @Around("execution(@TransactionEntryPoint * *.*(..))")
    public Object traceTransactionEntryPointAccess(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return handleTransaction(proceedingJoinPoint);
    }

    @Around("execution(@TransactionComponent * *.*(..))")
    public Object traceTransactionComponentAccess(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return handleTransactionComponent(proceedingJoinPoint);
    }

    private Object handleTransaction(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if(isRecording) {
            MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

            rootTxInfo = new TxInfo(
                    proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName(),
                    proceedingJoinPoint.getSignature().getName(),
                    methodSignature.getMethod().getAnnotation(TransactionEntryPoint.class).value());
            txStack = new Stack<>();
            txStack.push(rootTxInfo);
        }

        Object result = proceedingJoinPoint.proceed();

        if(isRecording) {
            txStack.pop();
            txStack = null;
        }

        return result;
    }

    private Object handleTransactionComponent(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if(isRecording) {
            MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

            TxInfo txInfo = new TxInfo(
                    proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName(),
                    proceedingJoinPoint.getSignature().getName(),
                    methodSignature.getMethod().getAnnotation(TransactionComponent.class).value());

            txStack.peek().components.add(txInfo);
            txStack.push(txInfo);
        }

        Object result = proceedingJoinPoint.proceed();

        if(isRecording) {
            txStack.pop();
        }

        return result;
    }
}
