package ru.kpfu.itis.kropinov.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.kropinov.dto.NoteCreateDto;
import ru.kpfu.itis.kropinov.dto.NoteEditDto;
import ru.kpfu.itis.kropinov.dto.NoteResponseDto;
import ru.kpfu.itis.kropinov.exception.NoteAccessDeniedException;
import ru.kpfu.itis.kropinov.exception.NoteNotExistsException;
import ru.kpfu.itis.kropinov.model.Note;
import ru.kpfu.itis.kropinov.model.User;
import ru.kpfu.itis.kropinov.repository.NoteRepository;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Transactional(readOnly = true)
    public List<NoteResponseDto> getAllNotesDto() {
        return noteRepository.findAll().stream()
                .map(n -> new NoteResponseDto(
                        n.getId(),
                        n.getTitle(),
                        n.getContent(),
                        n.getCreatedAt(),
                        n.isPublic(),
                        n.getAuthor().getEmail()
                ))
                .toList();
    }

    public void deleteNoteById(long id) {
        noteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Note> getNotesByUser(User user) {
        return noteRepository.findByAuthor(user);
    }

    @Transactional(readOnly = true)
    public List<Note> getPublicNotes() {
        return noteRepository.findByIsPublicTrue();
    }

    @Transactional
    public void createNote(NoteCreateDto noteCreateDto, User author) {
        Note newNote = new Note(noteCreateDto, author);
        noteRepository.save(newNote);
    }

    @Transactional(readOnly = true)
    public NoteEditDto getNoteEditDto(long noteId, User author) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(NoteNotExistsException::new);
        checkNoteIsAuthoredByUser(noteId, author);
        return new NoteEditDto(note.getTitle(), note.getContent(), note.isPublic());
    }

    @Transactional
    public void editNote(long noteId, NoteEditDto noteEditDto, User author) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(NoteNotExistsException::new);
        checkNoteIsAuthoredByUser(noteId, author);
        updateNote(note, noteEditDto);
    }

    @Transactional
    public void deleteNote(long noteId, User author) {
        checkNoteIsAuthoredByUser(noteId, author);
        noteRepository.deleteById(noteId);
    }

    private void updateNote(Note note, NoteEditDto noteEditDto) {
        note.setTitle(noteEditDto.getTitle());
        note.setContent(noteEditDto.getContent());
        note.setPublic(noteEditDto.isPublic());
    }

    private void checkNoteIsAuthoredByUser(Long noteId, User user) {
        if (!noteRepository.existsByIdAndAuthor(noteId, user)) {
            throw new NoteAccessDeniedException();
        }
    }
}
