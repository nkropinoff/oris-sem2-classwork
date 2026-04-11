package ru.kpfu.itis.kropinov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.kpfu.itis.kropinov.dto.UserCreateDto;
import ru.kpfu.itis.kropinov.dto.UserResponseDto;
import ru.kpfu.itis.kropinov.dto.UserUpdateDto;
import ru.kpfu.itis.kropinov.service.UserService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllUsers() {
        assertTrue(true);
    }

    @Test
    public void testGetUsers() throws Exception {
        UserResponseDto userResponseDto = new UserResponseDto(1, "Ivan");
        given(userService.getAllUsers()).willReturn(List.of(userResponseDto));
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Ivan"));
    }

    @Test
    public void testGetUser() throws Exception {
        UserResponseDto responseDto = new UserResponseDto(1, "Ivan");
        given(userService.getById(1L)).willReturn(responseDto);

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ivan"));
    }

    @Test
    public void testAddUser() throws Exception {
        UserCreateDto createDto = new UserCreateDto("Petr");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto))
                        .with(csrf())
                        .with(user("user").roles("USER")))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserUpdateDto updateDto = new UserUpdateDto(1, "Ivan Updated");
        UserResponseDto responseDto = new UserResponseDto(1, "Ivan Updated");
        given(userService.update(updateDto)).willReturn(responseDto);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .with(csrf())
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ivan Updated"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteById(1L);

        mockMvc.perform(delete("/users/1")
                        .with(csrf())
                        .with(user("user").roles("USER")))
                .andExpect(status().isNoContent());

        verify(userService).deleteById(1L);
    }

}
