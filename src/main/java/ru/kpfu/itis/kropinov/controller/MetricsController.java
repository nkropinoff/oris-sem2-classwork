package ru.kpfu.itis.kropinov.controller;

import jakarta.el.MethodNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.kropinov.service.MetricsService;

@Controller()
@RequestMapping("/metrics")
public class MetricsController {

    private final MetricsService service;

    public MetricsController(MetricsService service) {
        this.service = service;
    }
    @GetMapping
    public String getMetricsPage() {
        return "metrics";
    }

    @GetMapping("/success-execution")
    public String searchSuccessMetrics(@RequestParam String methodName, Model model) {
        model.addAttribute("methodName", methodName);
        try {
            model.addAttribute("metrics", service.getExecutionSuccessMetrics(methodName));
        } catch (MethodNotFoundException e) {
            model.addAttribute("errorMessage",
                    "No metrics found for method: \"" + methodName + "\"");
        }
        return "metrics";
    }

    @GetMapping("/time-execution")
    public String searchTimeMetrics(
            @RequestParam String methodName,
            @RequestParam(defaultValue = "95") Integer percentile,
            Model model) {
        model.addAttribute("timeMethodName", methodName);
        model.addAttribute("percentile", percentile);
        try {
            model.addAttribute("timeMetrics", service.getExecutionTimeMetrics(methodName, percentile));
        } catch (MethodNotFoundException e) {
            model.addAttribute("timeErrorMessage",
                    "No metrics found for method: \"" + methodName + "\"");
        }
        return "metrics";
    }


}
