package ru.kpfu.itis.kropinov.dto;

import java.util.List;

public record TimeMetricsDto(
        List<Long> durations,
        Long percentileValue,
        Double averageDuration
) {
}
