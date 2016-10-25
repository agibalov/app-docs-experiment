package me.loki2302.spring.advanced;

import me.loki2302.spring.TransactionComponent;
import me.loki2302.spring.TransactionEntryPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.ArrayList;
import java.util.List;

@Aspect
public class NewTransactionRecorder {
    private List<TransactionRecord> transactionRecords;

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
                return handleTransaction(proceedingJoinPoint);
            }
        }

        return proceedingJoinPoint.proceed();
    }

    @Around("execution(@me.loki2302.spring.TransactionEntryPoint * *.*(..))")
    public Object traceTransactionEntryPointAccess(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return handleTransaction(proceedingJoinPoint);
    }

    @Around("execution(@me.loki2302.spring.TransactionComponent * *.*(..))")
    public Object traceTransactionComponentAccess(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return handleTransaction(proceedingJoinPoint);
    }

    public void resetTransaction() {
        transactionRecords = new ArrayList<>();
    }

    public List<TransactionRecord> getTransactionRecords() {
        return transactionRecords;
    }

    private Object handleTransaction(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Signature signature = proceedingJoinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        if(transactionRecords != null) {
            transactionRecords.add(makeTransactionRecord("BEFORE", className, methodName));
        }

        Object result = proceedingJoinPoint.proceed();

        if(transactionRecords != null) {
            transactionRecords.add(makeTransactionRecord("AFTER", className, methodName));
        }

        return result;
    }

    private static TransactionRecord makeTransactionRecord(String comment, String className, String methodName) {
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.comment = comment;
        transactionRecord.className = className;
        transactionRecord.methodName = methodName;
        return transactionRecord;
    }

    public static class TransactionRecord {
        public String comment;
        public String className;
        public String methodName;
    }
}
