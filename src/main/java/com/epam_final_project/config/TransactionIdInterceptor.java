package com.epam_final_project.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class TransactionIdInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(TransactionIdInterceptor.class);
    private static final String TRANSACTION_ID_KEY = "transactionId";
    private static final String START_TIME_ATTRIBUTE = "transactionStartTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Generate unique transaction ID
        String transactionId = UUID.randomUUID().toString();

        // Store in MDC
        MDC.put(TRANSACTION_ID_KEY, transactionId);

        // Store start time for duration calculation
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME_ATTRIBUTE, startTime);

        // Log incoming request
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        String fullUri = queryString != null ? requestURI + "?" + queryString : requestURI;

        log.info("[TRANSACTION START] method={} uri={} transactionId={}", method, fullUri, transactionId);

        response.setHeader("X-Transaction-ID", transactionId);

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {
        try {
            // Calculate request duration
            Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
            long duration = (startTime != null) ? System.currentTimeMillis() - startTime : -1;

            // Log request completion
            String method = request.getMethod();
            String requestURI = request.getRequestURI();
            String queryString = request.getQueryString();
            String fullUri = queryString != null ? requestURI + "?" + queryString : requestURI;
            int status = response.getStatus();

            if (ex != null) {
                log.error("[TRANSACTION END] method={} uri={} status={} duration={}ms transactionId={} - Exception: {}",
                        method, fullUri, status, duration, MDC.get(TRANSACTION_ID_KEY), ex.getMessage(), ex);
            } else {
                log.info("[TRANSACTION END] method={} uri={} status={} duration={}ms transactionId={}",
                        method, fullUri, status, duration, MDC.get(TRANSACTION_ID_KEY));
            }
        } finally {
            // Always clean up MDC to prevent memory leaks
            MDC.remove(TRANSACTION_ID_KEY);
            request.removeAttribute(START_TIME_ATTRIBUTE);
        }
    }
}