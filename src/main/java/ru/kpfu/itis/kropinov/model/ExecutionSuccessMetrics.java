package ru.kpfu.itis.kropinov.model;

import java.util.concurrent.atomic.AtomicInteger;

public class ExecutionSuccessMetrics {

    AtomicInteger successCount = new AtomicInteger(0);
    AtomicInteger failureCount = new AtomicInteger(0);

    public void increaseSuccess() {
        successCount.incrementAndGet();
    }

    public void increaseFailure() {
        failureCount.incrementAndGet();
    }

    public Integer getSuccessCount() {
        return successCount.get();
    }
    public Integer getFailureCount() {
        return failureCount.get();
    }
}
