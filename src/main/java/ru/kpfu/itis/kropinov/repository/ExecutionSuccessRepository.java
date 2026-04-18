package ru.kpfu.itis.kropinov.repository;

import org.springframework.stereotype.Component;
import ru.kpfu.itis.kropinov.aop.Loggable;
import ru.kpfu.itis.kropinov.model.ExecutionSuccessMetrics;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ExecutionSuccessRepository {

    private final Map<String, ExecutionSuccessMetrics> metrics = new ConcurrentHashMap<>();

    @Loggable
    public void increaseSuccess(String methodName) {
        if (metrics.get(methodName) == null) {
            metrics.put(methodName, new ExecutionSuccessMetrics());
        }
        metrics.get(methodName).increaseSuccess();
    }

    @Loggable
    public void increaseFailure(String methodName) {
        if (metrics.get(methodName) == null) {
            metrics.put(methodName, new ExecutionSuccessMetrics());
        }
        metrics.get(methodName).increaseFailure();
    }

    public Optional<ExecutionSuccessMetrics> getMetrics(String methodName) {
        return Optional.ofNullable(metrics.get(methodName));
    }

}
