package ru.kpfu.itis.kropinov.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kpfu.itis.kropinov.dto.NoteCreateDto;
import ru.kpfu.itis.kropinov.dto.NoteEditDto;
import ru.kpfu.itis.kropinov.dto.NoteResponseDto;
import ru.kpfu.itis.kropinov.exception.NoteAccessDeniedException;
import ru.kpfu.itis.kropinov.exception.NoteNotExistsException;
import ru.kpfu.itis.kropinov.model.Note;
import ru.kpfu.itis.kropinov.model.User;
import ru.kpfu.itis.kropinov.repository.NoteRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    private User buildUser(long id, String email) {
        return new User(id, email);
    }

    private Note buildNote(long id, String title, String content, User author, boolean isPublic) {
        Note note = new Note();
        note.setId(id);
        note.setTitle(title);
        note.setContent(content);
        note.setAuthor(author);
        note.setCreatedAt(Instant.now());
        note.setPublic(isPublic);
        return note;
    }

    @Test
    void getAllNotesDto_returnsMappedDtos() {
        User author = buildUser(1L, "author@mail.com");
        Note note = buildNote(1L, "My Note", "Content text", author, true);
        given(noteRepository.findAll()).willReturn(List.of(note));

        List<NoteResponseDto> result = noteService.getAllNotesDto();

        assertEquals(1, result.size());
        assertEquals("My Note", result.get(0).title());
        assertEquals("author@mail.com", result.get(0).authorEmail());
    }

    @Test
    void getAllNotesDto_emptyRepository_returnsEmptyList() {
        given(noteRepository.findAll()).willReturn(List.of());

        assertTrue(noteService.getAllNotesDto().isEmpty());
    }

    @Test
    void deleteNoteById_callsRepositoryDeleteById() {
        noteService.deleteNoteById(5L);

        verify(noteRepository, times(1)).deleteById(5L);
    }

    @Test
    void getNotesByUser_returnsNotesForGivenUser() {
        User user = buildUser(1L, "u@mail.com");
        Note note = buildNote(1L, "T", "C", user, false);
        given(noteRepository.findByAuthor(user)).willReturn(List.of(note));

        List<Note> result = noteService.getNotesByUser(user);

        assertEquals(1, result.size());
        assertEquals(user, result.get(0).getAuthor());
    }

    @Test
    void getNotesByUser_noNotes_returnsEmptyList() {
        User user = buildUser(1L, "u@mail.com");
        given(noteRepository.findByAuthor(user)).willReturn(List.of());

        assertTrue(noteService.getNotesByUser(user).isEmpty());
    }

    @Test
    void getPublicNotes_returnsOnlyPublicNotes() {
        User user = buildUser(1L, "u@mail.com");
        Note publicNote = buildNote(1L, "Public Note", "Content", user, true);
        given(noteRepository.findByIsPublicTrue()).willReturn(List.of(publicNote));

        List<Note> result = noteService.getPublicNotes();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isPublic());
    }

    @Test
    void createNote_savesNewNoteToRepository() {
        User author = buildUser(1L, "u@mail.com");
        NoteCreateDto dto = mock(NoteCreateDto.class);
        given(dto.getTitle()).willReturn("New Note");
        given(dto.getContent()).willReturn("Some content");
        given(dto.isPublic()).willReturn(false);

        noteService.createNote(dto, author);

        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    void getNoteEditDto_authorizedUser_returnsEditDto() {
        User author = buildUser(1L, "u@mail.com");
        Note note = buildNote(1L, "Title", "Content", author, false);
        given(noteRepository.findById(1L)).willReturn(Optional.of(note));
        given(noteRepository.existsByIdAndAuthor(1L, author)).willReturn(true);

        NoteEditDto result = noteService.getNoteEditDto(1L, author);

        assertEquals("Title", result.getTitle());
        assertEquals("Content", result.getContent());
        assertFalse(result.isPublic());
    }

    @Test
    void getNoteEditDto_noteNotFound_throwsNoteNotExistsException() {
        User author = buildUser(1L, "u@mail.com");
        given(noteRepository.findById(99L)).willReturn(Optional.empty());

        assertThrows(NoteNotExistsException.class,
                () -> noteService.getNoteEditDto(99L, author));
    }

    @Test
    void getNoteEditDto_notOwner_throwsNoteAccessDeniedException() {
        User other = buildUser(2L, "other@mail.com");
        Note note = buildNote(1L, "T", "C", buildUser(1L, "owner@mail.com"), false);
        given(noteRepository.findById(1L)).willReturn(Optional.of(note));
        given(noteRepository.existsByIdAndAuthor(1L, other)).willReturn(false);

        assertThrows(NoteAccessDeniedException.class,
                () -> noteService.getNoteEditDto(1L, other));
    }

    @Test
    void editNote_authorizedUser_updatesNoteFields() {
        User author = buildUser(1L, "u@mail.com");
        Note note = buildNote(1L, "Old Title", "Old Content", author, false);
        NoteEditDto editDto = new NoteEditDto("New Title", "New Content", true);
        given(noteRepository.findById(1L)).willReturn(Optional.of(note));
        given(noteRepository.existsByIdAndAuthor(1L, author)).willReturn(true);

        noteService.editNote(1L, editDto, author);

        assertEquals("New Title", note.getTitle());
        assertEquals("New Content", note.getContent());
        assertTrue(note.isPublic());
    }

    @Test
    void editNote_noteNotFound_throwsNoteNotExistsException() {
        User author = buildUser(1L, "u@mail.com");
        given(noteRepository.findById(99L)).willReturn(Optional.empty());

        assertThrows(NoteNotExistsException.class,
                () -> noteService.editNote(99L, new NoteEditDto("T", "C", false), author));
    }

    @Test
    void editNote_notOwner_throwsNoteAccessDeniedException() {
        User other = buildUser(2L, "other@mail.com");
        Note note = buildNote(1L, "T", "C", buildUser(1L, "owner@mail.com"), false);
        given(noteRepository.findById(1L)).willReturn(Optional.of(note));
        given(noteRepository.existsByIdAndAuthor(1L, other)).willReturn(false);

        assertThrows(NoteAccessDeniedException.class,
                () -> noteService.editNote(1L, new NoteEditDto("T", "C", false), other));
    }

    @Test
    void deleteNote_authorizedUser_callsDeleteById() {
        User author = buildUser(1L, "u@mail.com");
        given(noteRepository.existsByIdAndAuthor(1L, author)).willReturn(true);

        noteService.deleteNote(1L, author);

        verify(noteRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteNote_notOwner_throwsNoteAccessDeniedException() {
        User other = buildUser(2L, "other@mail.com");
        given(noteRepository.existsByIdAndAuthor(1L, other)).willReturn(false);

        assertThrows(NoteAccessDeniedException.class,
                () -> noteService.deleteNote(1L, other));
        verify(noteRepository, never()).deleteById(any());
    }
}