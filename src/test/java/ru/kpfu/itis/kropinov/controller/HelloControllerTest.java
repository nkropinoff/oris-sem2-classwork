package ru.kpfu.itis.kropinov.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.kpfu.itis.kropinov.repository.UserRepository;
import ru.kpfu.itis.kropinov.service.HelloService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        value = HelloController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        }
)
public class HelloControllerTest {

    @MockitoBean
    private HelloService helloService;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHello_WithName() throws Exception {
        given(helloService.sayHello("Nikita")).willReturn("Hello, Nikita!");

        mockMvc.perform(get("/hello").param("name", "Nikita"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Nikita!"));
    }

    @Test
    public void testHello_WithoutName() throws Exception {
        given(helloService.sayHello(null)).willReturn("Hello, World!");

        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, World!"));
    }
}