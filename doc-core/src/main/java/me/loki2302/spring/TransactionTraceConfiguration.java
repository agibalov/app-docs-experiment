package me.loki2302.spring;

import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
public class TransactionTraceConfiguration {
    @Bean
    public TransactionRecorder transactionTraceAspect() {
        return new TransactionRecorder();
    }
}
