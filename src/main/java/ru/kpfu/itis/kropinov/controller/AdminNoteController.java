package ru.kpfu.itis.kropinov.controller;

import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.kropinov.dto.NoteResponseDto;
import ru.kpfu.itis.kropinov.model.Note;
import ru.kpfu.itis.kropinov.service.NoteService;

import java.util.List;

@RestController()
@RequestMapping("/admin/notes")
public class AdminNoteController {

    private final NoteService noteService;

    public AdminNoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public List<NoteResponseDto> getAllNotes() {
        return noteService.getAllNotesDto();
    }

    @DeleteMapping("/{id}")
    public void  deleteNote(@PathVariable("id") long id) {
        noteService.deleteNoteById(id);
    }

}
