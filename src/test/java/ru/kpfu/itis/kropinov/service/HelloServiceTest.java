package ru.kpfu.itis.kropinov.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class HelloServiceTest {

    @InjectMocks
    private HelloService helloService;

    @Test
    void sayHello_withName_returnsFormattedString() {
        String result = helloService.sayHello("Nikita");
        assertEquals("Hello, Nikita!", result);
    }

    @Test
    void sayHello_withEmptyName_returnsFormattedString() {
        String result = helloService.sayHello("");
        assertEquals("Hello, !", result);
    }
}