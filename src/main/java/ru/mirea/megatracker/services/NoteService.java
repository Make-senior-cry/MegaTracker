package ru.mirea.megatracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mirea.megatracker.models.Note;
import ru.mirea.megatracker.models.User;
import ru.mirea.megatracker.repositories.NotesRepository;
import ru.mirea.megatracker.repositories.UsersRepository;

import java.util.Optional;

@Service
public class NoteService {
    private final NotesRepository notesRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public NoteService(NotesRepository notesRepository, UsersRepository usersRepository) {
        this.notesRepository = notesRepository;
        this.usersRepository = usersRepository;
    }

    public void addNoteForCoin(String email, String ticker, String newNote) {
        Optional<User> user = usersRepository.findByEmail(email);

        if (user.isPresent()) {
            Note note = new Note();
            note.setUser(user.get());
            note.setNote(newNote);
            note.setTicker(ticker);
            notesRepository.save(note);
        }
    }
}
