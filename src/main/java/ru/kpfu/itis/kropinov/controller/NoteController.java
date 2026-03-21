package ru.kpfu.itis.kropinov.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.kropinov.dto.NoteCreateDto;
import ru.kpfu.itis.kropinov.dto.NoteEditDto;
import ru.kpfu.itis.kropinov.model.Note;
import ru.kpfu.itis.kropinov.model.User;
import ru.kpfu.itis.kropinov.service.CustomUserDetails;
import ru.kpfu.itis.kropinov.service.NoteService;

import java.util.List;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public String getOwnNotes(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userDetails.getUser();
        List<Note> userNotes = noteService.getNotesByUser(user);
        model.addAttribute("userNotes", userNotes);
        return "notes";
    }

    @GetMapping("/public")
    public String getPublicNotes(Model model) {
        List<Note> publicNotes = noteService.getPublicNotes();
        model.addAttribute("publicNotes", publicNotes);
        return "public_notes";
    }

    @GetMapping("/create")
    public String createNoteForm(Model model) {
        model.addAttribute("createNote", new NoteCreateDto());
        model.addAttribute("formAction", "/notes/create");
        return "note_form";
    }

    @PostMapping("/create")
    public String createNote(@ModelAttribute NoteCreateDto noteCreateDto, BindingResult bindingResult,
                             @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/notes/create");
            return "note_form";
        }
        noteService.createNote(noteCreateDto, userDetails.getUser());
        return "redirect:/notes";
    }

    @GetMapping("/{id}/edit")
    public String editNoteForm(@PathVariable("id") long noteId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        NoteEditDto noteEditDto = noteService.getNoteEditDto(noteId, userDetails.getUser());
        model.addAttribute("note", noteEditDto);
        model.addAttribute("formAction", "/notes/" + noteId + "/edit");
        return "note_form";
    }

    @PostMapping("/{id}/edit")
    public String editNote(@PathVariable("id") long noteId, @ModelAttribute NoteEditDto noteEditDto, BindingResult bindingResult,
                           Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/notes/" + noteId + "/edit");
            return "note_form";
        }
        noteService.editNote(noteId, noteEditDto, userDetails.getUser());
        return "redirect:/notes";
    }

    @PostMapping("/{id}/delete")
    public String deleteNote(@PathVariable("id") long noteId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        noteService.deleteNote(noteId, userDetails.getUser());
        return "redirect:/notes";
    }
}
