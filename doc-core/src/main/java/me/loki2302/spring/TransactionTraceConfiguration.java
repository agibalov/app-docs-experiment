package me.loki2302.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionTraceConfiguration {
    @Bean
    public TransactionRecorder transactionTraceAspect() {
        return new TransactionRecorder();
    }
}
