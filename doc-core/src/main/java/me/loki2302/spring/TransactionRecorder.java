package me.loki2302.spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.ArrayList;
import java.util.List;

@Aspect
public class TransactionRecorder {
    private final static Logger LOGGER = LoggerFactory.getLogger(TransactionRecorder.class);

    private List<TransactionEvent> transactionEvents;

    @Around("execution(public * org.springframework.data.repository.Repository+.*(..))")
    public Object traceSpringRepositoryAccess(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Signature signature = proceedingJoinPoint.getSignature();
        if(signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature)signature;

            TransactionComponent transactionComponent = AnnotationUtils.getAnnotation(
                    methodSignature.getMethod(),
                    TransactionComponent.class);
            if(transactionComponent != null) {
                return handleTransaction(proceedingJoinPoint, transactionComponent.value());
            }
        }

        return proceedingJoinPoint.proceed();
    }

    @Around("execution(@me.loki2302.spring.TransactionComponent * *.*(..))")
    public Object traceTransactionComponentAccess(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        TransactionComponent transactionComponent = AnnotationUtils.getAnnotation(
                methodSignature.getMethod(),
                TransactionComponent.class);

        return handleTransaction(proceedingJoinPoint, transactionComponent.value());
    }

    public void resetTransaction() {
        transactionEvents = new ArrayList<>();
    }

    public List<TransactionEvent> getTransactionEvents() {
        return transactionEvents;
    }

    private Object handleTransaction(ProceedingJoinPoint proceedingJoinPoint, String comment) throws Throwable {
        Signature signature = proceedingJoinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        if(transactionEvents != null) {
            TransactionEvent transactionEvent = makeTransactionEvent("BE", comment, TransactionEventType.Enter, className, methodName);
            transactionEvents.add(transactionEvent);
            LOGGER.info("tracing {}", transactionEvent);
        }

        Object result = proceedingJoinPoint.proceed();

        if(transactionEvents != null) {
            TransactionEvent transactionEvent = makeTransactionEvent("BE", comment, TransactionEventType.Leave, className, methodName);
            transactionEvents.add(transactionEvent);
            LOGGER.info("tracing {}", transactionEvent);
        }

        return result;
    }

    private static TransactionEvent makeTransactionEvent(
            String tag,
            String comment,
            TransactionEventType eventType,
            String className,
            String methodName) {

        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.tag = tag;
        transactionEvent.comment = comment;
        transactionEvent.eventType = eventType;
        transactionEvent.className = className;
        transactionEvent.methodName = methodName;
        return transactionEvent;
    }
}
