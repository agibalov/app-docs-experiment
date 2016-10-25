package me.loki2302.spring.advanced;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionLogConfiguration {
    @Bean
    public NewTransactionRecorder newTransactionRecorder() {
        return new NewTransactionRecorder();
    }

    @Bean
    public AddBackendTransactionHeaderFilter addBackendTransactionHeaderFilter() {
        return new AddBackendTransactionHeaderFilter();
    }
}
