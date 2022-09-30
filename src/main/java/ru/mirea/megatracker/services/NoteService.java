package ru.mirea.megatracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mirea.megatracker.repositories.NotesRepository;

@Service
public class NoteService {
    private final NotesRepository notesRepository;

    @Autowired
    public NoteService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }


}
