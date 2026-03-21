package ru.kpfu.itis.kropinov.model;

import jakarta.persistence.*;
import ru.kpfu.itis.kropinov.dto.NoteCreateDto;

import java.time.Instant;

@Entity
@Table(name= "notes" )
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private Instant  createdAt;

    private boolean isPublic;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Note() {}

    public Note(NoteCreateDto noteCreateDto, User author) {
        this.title = noteCreateDto.getTitle();
        this.content = noteCreateDto.getContent();
        this.createdAt = Instant.now();
        this.author = author;
        this.isPublic = noteCreateDto.isPublic();
    }

}
