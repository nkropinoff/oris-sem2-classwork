<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Method Metrics</title>
</head>
<body>

<h1>Method Metrics</h1>

<h2>Success Metrics</h2>
<form method="get" action="/metrics/success-execution">
    <label for="methodName">Название метода:</label>
    <input type="text" id="methodName" name="methodName"
           value="${methodName!''}" placeholder="e.g. createOrder" required />
    <button type="submit">Найти</button>
</form>

<#if errorMessage??>
    <p style="color: red;">${errorMessage}</p>
</#if>

<#if metrics??>
    <hr>
    <h3>Результаты для: ${methodName!''}</h3>
    <p>Успешных вызовов: <strong>${metrics.success()}</strong></p>
    <p>Неуспешных вызовов: <strong>${metrics.failure()}</strong></p>
    <p>Всего: <strong>${metrics.success() + metrics.failure()}</strong></p>
</#if>

<hr>

<h2>Time Metrics</h2>
<form method="get" action="/metrics/time-execution">
    <label for="timeMethodName">Название метода:</label>
    <input type="text" id="timeMethodName" name="methodName"
           value="${timeMethodName!''}" placeholder="e.g. createOrder" required />
    <label for="percentile">Перцентиль (0-100):</label>
    <input type="number" id="percentile" name="percentile"
           value="${percentile!95}" min="0" max="100" required />
    <button type="submit">Найти</button>
</form>

<#if timeErrorMessage??>
    <p style="color: red;">${timeErrorMessage}</p>
</#if>

<#if timeMetrics??>
    <hr>
    <h3>Результаты для: ${timeMethodName!''}</h3>
    <p>Перцентиль P${percentile!''}: <strong>${timeMetrics.percentileValue()} нс</strong></p>
    <p>Среднее время: <strong>${timeMetrics.averageDuration()?string["0.##"]} нс</strong></p>
    <p>Всего вызовов: <strong>${timeMetrics.durations()?size}</strong></p>
    <p>Все замеры (нс):
        <#list timeMetrics.durations() as d>${d}<#sep>; </#sep></#list>
    </p>
</#if>

</body>
</html>