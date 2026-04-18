package ru.kpfu.itis.kropinov.repository;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ExecutionTimeRepository {

    private final Map<String, List<Long>> executionDurations = new ConcurrentHashMap<>();

    public void add(String methodName, Long duration) {
        executionDurations.computeIfAbsent(methodName, k -> new ArrayList<>());
        executionDurations.get(methodName).add(duration);
    }

    public Optional<List<Long>> getDurations(String methodName) {
        return Optional.ofNullable(executionDurations.get(methodName));
    }
}
