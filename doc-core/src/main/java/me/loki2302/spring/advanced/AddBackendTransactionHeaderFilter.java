package me.loki2302.spring.advanced;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class AddBackendTransactionHeaderFilter extends OncePerRequestFilter {
    private final static Logger LOGGER = LoggerFactory.getLogger(AddBackendTransactionHeaderFilter.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NewTransactionRecorder newTransactionRecorder;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // TODO: replace with transaction IDs or make it track events in per-request scope
        if(!request.getRequestURI().contains("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        LOGGER.info("Resetting TX for {} {}", request.getMethod(), request.getRequestURI());
        newTransactionRecorder.resetTransaction();

        ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(request, contentCachingResponseWrapper);

        List<TransactionEvent> transactionEvents =
                newTransactionRecorder.getTransactionEvents();

        String transactionLogJson;
        try {
            transactionLogJson = objectMapper.writeValueAsString(transactionEvents);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        contentCachingResponseWrapper.setHeader("X-BackEnd-Transaction", transactionLogJson);
        contentCachingResponseWrapper.copyBodyToResponse();
    }
}
