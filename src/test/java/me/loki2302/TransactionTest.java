package me.loki2302;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.loki2302.core.*;
import me.loki2302.core.snippets.SequenceDiagramSnippet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        App.class,
        TransactionTest.Config.class
}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransactionTest {
    @Rule
    public final SnippetWriter snippetWriter = new SnippetWriter(System.getProperty("snippetsDir"));

    @Autowired
    private TransactionTraceAspect transactionTraceAspect;

    @Test
    public void itShouldCreateANote() throws JsonProcessingException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        NoteDto noteDto = new NoteDto();
        noteDto.text = "hello world";

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity(
                "http://localhost:8080/api/notes",
                noteDto,
                Void.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("http://localhost:8080/api/notes/1", responseEntity.getHeaders().getLocation().toString());

        TxInfo txInfo = transactionTraceAspect.getLastTxInfo();

        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(txInfo));
        List<String> links = traverse(txInfo);

        snippetWriter.write("sequenceDiagram.puml", new SequenceDiagramSnippet(txInfo.comment, links));
    }

    // TODO: clean this up
    private final static List<String> traverse(TxInfo txInfo) {
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

    @Configuration
    @Import(AopAutoConfiguration.class)
    public static class Config {
        @Bean
        public TransactionTraceAspect transactionTraceAspect() {
            return new TransactionTraceAspect();
        }
    }

    @Aspect
    public static class TransactionTraceAspect {
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

    public static class TxInfo {
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
}
