package ru.kpfu.itis.kropinov.dto;

public class NoteEditDto {
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

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NoteEditDto(String title, String content, boolean isPublic) {
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
    }

    public NoteEditDto() {}

}
