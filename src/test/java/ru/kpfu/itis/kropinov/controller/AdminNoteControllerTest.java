package ru.kpfu.itis.kropinov.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.kpfu.itis.kropinov.dto.NoteResponseDto;
import ru.kpfu.itis.kropinov.service.NoteService;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = AdminNoteController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        }
)
public class AdminNoteControllerTest {

    @MockitoBean
    private NoteService noteService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllNotes() throws Exception {
        NoteResponseDto note = new NoteResponseDto(
                1L, "Test Title", "Test Content", Instant.now(), true, "author@test.com"
        );
        given(noteService.getAllNotesDto()).willReturn(List.of(note));

        mockMvc.perform(get("/admin/notes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Test Title"))
                .andExpect(jsonPath("$[0].authorEmail").value("author@test.com"));
    }

    @Test
    public void testGetAllNotes_Empty() throws Exception {
        given(noteService.getAllNotesDto()).willReturn(List.of());

        mockMvc.perform(get("/admin/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testDeleteNote() throws Exception {
        doNothing().when(noteService).deleteNoteById(1L);

        mockMvc.perform(delete("/admin/notes/1"))
                .andExpect(status().isOk());

        verify(noteService).deleteNoteById(1L);
    }
}