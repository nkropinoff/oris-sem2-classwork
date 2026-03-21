package ru.kpfu.itis.kropinov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kpfu.itis.kropinov.model.Note;
import ru.kpfu.itis.kropinov.model.User;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByAuthor(User author);

    @Query("SELECT n FROM Note n JOIN FETCH n.author WHERE n.isPublic = true")
    List<Note> findByIsPublicTrue();

    boolean existsByIdAndAuthor(Long id, User author);
}
