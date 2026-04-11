package ru.kpfu.itis.kropinov.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractView;
import ru.kpfu.itis.kropinov.service.UserService;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = AuthController.class,
        excludeAutoConfiguration = {
                FreeMarkerAutoConfiguration.class,
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        }
)
public class AuthControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class ViewConfig {
        @Bean
        @Primary
        public ViewResolver viewResolver() {
            return (viewName, locale) -> {
                if (viewName.startsWith("redirect:") || viewName.startsWith("forward:")) {
                    return null;
                }
                return new AbstractView() {
                    @Override
                    protected void renderMergedOutputModel(Map<String, Object> model,
                                                           HttpServletRequest req, HttpServletResponse res) {
                    }
                };
            };
        }
    }

    @Test
    public void testGetRegister() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    public void testPostRegister_Success() throws Exception {
        doNothing().when(userService).register("user@test.com", "password");

        mockMvc.perform(post("/register")
                        .param("email", "user@test.com")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testPostRegister_Error() throws Exception {
        doThrow(new RuntimeException("Email already taken")).when(userService).register(any(), any());

        mockMvc.perform(post("/register")
                        .param("email", "taken@test.com")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void testGetVerification_ValidCode() throws Exception {
        given(userService.verifyUser("valid-code")).willReturn(true);

        mockMvc.perform(get("/verification").param("code", "valid-code"))
                .andExpect(status().isOk())
                .andExpect(view().name("email-verified"));
    }

    @Test
    public void testGetVerification_InvalidCode() throws Exception {
        given(userService.verifyUser("wrong-code")).willReturn(false);

        mockMvc.perform(get("/verification").param("code", "wrong-code"))
                .andExpect(status().isOk())
                .andExpect(view().name("verification-error"));
    }

    @Test
    public void testGetLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testGetRoot_RedirectsToIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));
    }
}