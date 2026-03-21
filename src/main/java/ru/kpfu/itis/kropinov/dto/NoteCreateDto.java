package ru.kpfu.itis.kropinov.dto;

import java.time.Instant;
//
public class NoteCreateDto {
    private String title;

    private String content;

    private boolean isPublic;


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

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
