package ru.kpfu.itis.kropinov.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.kpfu.itis.kropinov.dto.NoteCreateDto;
import ru.kpfu.itis.kropinov.dto.NoteEditDto;
import ru.kpfu.itis.kropinov.model.Note;
import ru.kpfu.itis.kropinov.model.User;
import ru.kpfu.itis.kropinov.service.CustomUserDetails;
import ru.kpfu.itis.kropinov.service.NoteService;
import ru.kpfu.itis.kropinov.service.NoteServiceTest;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = NoteController.class, excludeAutoConfiguration = FreeMarkerAutoConfiguration.class)
public class NoteControllerTest {

    @MockitoBean
    private NoteService noteService;

    @Autowired
    private MockMvc mockMvc;

    private CustomUserDetails mockUserDetails;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = mock(User.class);
        mockUserDetails = mock(CustomUserDetails.class);

        given(mockUserDetails.getUser()).willReturn(mockUser);
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .when(mockUserDetails).getAuthorities();
        given(mockUserDetails.isEnabled()).willReturn(true);
        given(mockUserDetails.isAccountNonExpired()).willReturn(true);
        given(mockUserDetails.isAccountNonLocked()).willReturn(true);
        given(mockUserDetails.isCredentialsNonExpired()).willReturn(true);
    }

    private RequestPostProcessor asUser() {
        return authentication(new UsernamePasswordAuthenticationToken(
                mockUserDetails, null, mockUserDetails.getAuthorities()
        ));
    }

    @Test
    public void testGetOwnNotes() throws Exception {
        Note note = mock(Note.class);
        given(noteService.getNotesByUser(any(User.class))).willReturn(List.of(note));

        mockMvc.perform(get("/notes/public")
                .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("public_notes"));
    }

    @Test
    public void testGetPublicNotes() throws Exception {
        given(noteService.getPublicNotes()).willReturn(List.of());

        mockMvc.perform(get("/notes/public")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("public_notes"))
                .andExpect(model().attributeExists("publicNotes"));
    }

    @Test
    public void testGetCreateNoteForm() throws Exception {
        mockMvc.perform(get("/notes/create").with(asUser()))
                .andExpect(status().isOk())
                .andExpect(view().name("note_form"))
                .andExpect(model().attributeExists("createNote"))
                .andExpect(model().attribute("formAction", "/notes/create"));
    }

    @Test
    public void testPostCreateNote_Success_RedirectsToNotes() throws Exception {
        doNothing().when(noteService).createNote(any(NoteCreateDto.class), eq(mockUser));

        mockMvc.perform(post("/notes/create")
                        .param("title", "My Note")
                        .param("content", "Some content")
                        .param("public", "false")
                        .with(csrf())
                        .with(asUser()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));
    }

    @Test
    public void testGetEditNoteForm_ReturnsNoteFormView() throws Exception {
        NoteEditDto editDto = new NoteEditDto("Old Title", "Old Content", false);
        given(noteService.getNoteEditDto(1L, mockUser)).willReturn(editDto);

        mockMvc.perform(get("/notes/1/edit").with(asUser()))
                .andExpect(status().isOk())
                .andExpect(view().name("note_form"))
                .andExpect(model().attributeExists("note"))
                .andExpect(model().attribute("formAction", "/notes/1/edit"));
    }

    @Test
    public void testPostEditNote_Success_RedirectsToNotes() throws Exception {
        doNothing().when(noteService).editNote(eq(1L), any(NoteEditDto.class), eq(mockUser));

        mockMvc.perform(post("/notes/1/edit")
                        .param("title", "Updated Title")
                        .param("content", "Updated content")
                        .param("public", "true")
                        .with(csrf())
                        .with(asUser()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));
    }

    @Test
    public void testPostDeleteNote_Success_RedirectsToNotes() throws Exception {
        doNothing().when(noteService).deleteNote(1L, mockUser);

        mockMvc.perform(post("/notes/1/delete")
                        .with(csrf())
                        .with(asUser()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));
    }
}