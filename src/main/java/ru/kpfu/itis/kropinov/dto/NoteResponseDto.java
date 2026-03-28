package ru.kpfu.itis.kropinov.dto;

import java.time.Instant;

public record NoteResponseDto(
        Long id,
        String title,
        String content,
        Instant createdAt,
        boolean isPublic,
        String authorEmail
) {}
