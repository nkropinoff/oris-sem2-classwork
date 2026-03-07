package ru.kpfu.itis.kropinov.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.kropinov.repository.UserRepository;
import ru.kpfu.itis.kropinov.repository.UserRepositoryHibernate;
import ru.kpfu.itis.kropinov.service.HelloService;

@RestController
public class HelloController {

    private final HelloService helloService;
    private final UserRepository userRepository;

    public HelloController(HelloService helloService, UserRepository userRepository) {
        this.helloService = helloService;
        this.userRepository = userRepository;
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(required = false, name = "name") String name) {
        return helloService.sayHello(name);
    }

}
