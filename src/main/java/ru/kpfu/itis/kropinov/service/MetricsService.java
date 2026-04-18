package ru.kpfu.itis.kropinov.service;

import jakarta.el.MethodNotFoundException;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.kropinov.dto.SuccessMetricsDto;
import ru.kpfu.itis.kropinov.dto.TimeMetricsDto;
import ru.kpfu.itis.kropinov.model.ExecutionSuccessMetrics;
import ru.kpfu.itis.kropinov.repository.ExecutionSuccessRepository;
import ru.kpfu.itis.kropinov.repository.ExecutionTimeRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class MetricsService {

    private final ExecutionSuccessRepository successRepository;
    private final ExecutionTimeRepository timeRepository;

    public MetricsService(ExecutionSuccessRepository successRepository, ExecutionTimeRepository timeRepository) {
        this.timeRepository = timeRepository;
        this.successRepository = successRepository;
    }

    public SuccessMetricsDto getExecutionSuccessMetrics(String methodName) {
        ExecutionSuccessMetrics metrics = successRepository.getMetrics(methodName).orElseThrow(
                () -> new MethodNotFoundException(methodName)
        );
        return new SuccessMetricsDto(metrics.getSuccessCount(), metrics.getFailureCount());
    }

    public TimeMetricsDto getExecutionTimeMetrics(String methodName, Integer percentile) {
        List<Long> durations = timeRepository.getDurations(methodName).orElseThrow(
                () -> new MethodNotFoundException(methodName)
        );
        System.out.println(durations);
        return new TimeMetricsDto(
                List.copyOf(durations),
                calcPercentile(durations, percentile),
                calcAverage(durations)
        );
    }

    private Long calcPercentile(List<Long> durations, Integer percentile) {
        durations.sort(Long::compare);
        int index = percentile == 0 ? 0 : (int) Math.ceil(percentile / 100.0 * durations.size()) - 1;
        return durations.get(index);
    }

    private Double calcAverage(List<Long> durations) {
        return durations.stream().mapToDouble(Long::doubleValue).average().getAsDouble();
    }

}
