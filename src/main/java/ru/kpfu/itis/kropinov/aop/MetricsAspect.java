package ru.kpfu.itis.kropinov.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.kropinov.repository.ExecutionSuccessRepository;
import ru.kpfu.itis.kropinov.repository.ExecutionTimeRepository;

@Aspect
@Component
public class MetricsAspect {

    private final ExecutionSuccessRepository executionSuccessRepository;
    private final ExecutionTimeRepository executionTimeRepository;

    public MetricsAspect(ExecutionSuccessRepository executionSuccessRepository, ExecutionTimeRepository executionTimeRepository) {
        this.executionSuccessRepository = executionSuccessRepository;
        this.executionTimeRepository = executionTimeRepository;
    }

    @Pointcut("@annotation(ExecutionSuccessMetrics)")
    public void metricsExecutionSuccess() {
    }

    @Around("metricsExecutionSuccess()")
    public Object executionSuccess(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("executionSuccess metrics");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();

        Object result;
        try {
            result = joinPoint.proceed();
            executionSuccessRepository.increaseSuccess(methodName);
            return result;
        } catch (Throwable throwable) {
            executionSuccessRepository.increaseFailure(methodName);
            throw new RuntimeException(throwable);
        }
    }


    @Pointcut("@annotation(ExecutionTimeMetrics)")
    public void metricsExecutionTime() {
    }

    @Around("metricsExecutionTime()")
    public Object executionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        Object result;
        try {
            long start = System.nanoTime();
            result = joinPoint.proceed();
            long finish = System.nanoTime();
            executionTimeRepository.add(methodName, finish - start);
            return result;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

}
