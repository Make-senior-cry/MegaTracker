package ru.mirea.megatracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.mirea.megatracker.interfaces.INoteService;
import ru.mirea.megatracker.models.Note;
import ru.mirea.megatracker.models.User;
import ru.mirea.megatracker.repositories.NotesRepository;
import ru.mirea.megatracker.repositories.UsersRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class NoteService implements INoteService {
    private final NotesRepository notesRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public NoteService(NotesRepository notesRepository, UsersRepository usersRepository) {
        this.notesRepository = notesRepository;
        this.usersRepository = usersRepository;
    }

    @Transactional
    @Override
    public void setNoteForCoin(String email, String ticker, String newNote) {
        Optional<User> user = usersRepository.findByEmail(email);
        Optional<Note> existingNote = notesRepository.findByUserAndTicker(user.get(), ticker);

        if (existingNote.isPresent()) {
            if (newNote.equals("") && !existingNote.get().isFavorite()) {
                notesRepository.delete(existingNote.get());
            } else {
                existingNote.get().setNote(newNote);
                notesRepository.save(existingNote.get());
            }
        } else {
            Note note = new Note();
            note.setUser(user.get());
            note.setNote(newNote);
            note.setTicker(ticker);
            notesRepository.save(note);
        }
    }

    @Transactional
    @Override
    public void setFavoriteForCoin(String email, String ticker, boolean isFavorite) {
        Optional<User> user = usersRepository.findByEmail(email);
        Optional<Note> existingNote = notesRepository.findByUserAndTicker(user.get(), ticker);

        if (existingNote.isPresent()) {
            if (!isFavorite && existingNote.get().getNote().equals("")) {
                notesRepository.delete(existingNote.get());
            } else {
                existingNote.get().setFavorite(isFavorite);
                notesRepository.save(existingNote.get());
            }
        } else {
            Note note = new Note();
            note.setTicker(ticker);
            note.setUser(user.get());
            note.setFavorite(isFavorite);
            note.setNote("");
            notesRepository.save(note);
        }
    }
}
