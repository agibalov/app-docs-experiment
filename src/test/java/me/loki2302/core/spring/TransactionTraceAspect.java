package me.loki2302.core.spring;

import me.loki2302.core.TransactionComponent;
import me.loki2302.core.TransactionEntryPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Stack;

@Aspect
public class TransactionTraceAspect {
    private TxInfo rootTxInfo;
    private Stack<TxInfo> txStack;

    public TxInfo getLastTxInfo() {
        return rootTxInfo;
    }

    @Around("execution(@me.loki2302.core.TransactionEntryPoint * *.*(..))")
    public Object traceTransaction(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature)proceedingJoinPoint.getSignature();

        rootTxInfo = new TxInfo(
                proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName(),
                proceedingJoinPoint.getSignature().getName(),
                methodSignature.getMethod().getAnnotation(TransactionEntryPoint.class).value());
        txStack = new Stack<>();
        txStack.push(rootTxInfo);

        Object result = proceedingJoinPoint.proceed();

        txStack.pop();
        txStack = null;

        return result;
    }

    @Around("execution(@me.loki2302.core.TransactionComponent * *.*(..))")
    public Object traceTransactionComponent(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature)proceedingJoinPoint.getSignature();

        TxInfo txInfo = new TxInfo(
                proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName(),
                proceedingJoinPoint.getSignature().getName(),
                methodSignature.getMethod().getAnnotation(TransactionComponent.class).value());

        txStack.peek().components.add(txInfo);
        txStack.push(txInfo);

        Object result = proceedingJoinPoint.proceed();

        txStack.pop();

        return result;
    }
}
