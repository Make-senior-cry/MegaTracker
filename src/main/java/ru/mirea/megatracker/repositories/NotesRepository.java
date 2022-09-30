package ru.mirea.megatracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.megatracker.models.Note;

import java.util.Optional;

@Repository
public interface NotesRepository extends JpaRepository<Note, String> {
    Optional<Note> findByTicker(String ticker);
}
