package ru.kpfu.itis.kropinov.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kpfu.itis.kropinov.aop.ExecutionSuccessMetrics;
import ru.kpfu.itis.kropinov.aop.ExecutionTimeMetrics;
import ru.kpfu.itis.kropinov.aop.Loggable;

@Controller
public class IndexController {

    @GetMapping("/index")
    @Loggable
    @ExecutionSuccessMetrics
    @ExecutionTimeMetrics
    public String index() {
        return "index";
    }

}
