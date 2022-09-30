package ru.mirea.megatracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.megatracker.models.Note;

@Repository
public interface NotesRepository extends JpaRepository<Note, String> {

}
