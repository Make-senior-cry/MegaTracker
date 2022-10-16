package ru.mirea.megatracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.mirea.megatracker.exceptions.UserNotFoundException;
import ru.mirea.megatracker.models.Note;
import ru.mirea.megatracker.models.User;
import ru.mirea.megatracker.repositories.NotesRepository;
import ru.mirea.megatracker.repositories.UsersRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class NoteServiceImpl implements ru.mirea.megatracker.interfaces.NoteService {
    private final NotesRepository notesRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public NoteServiceImpl(NotesRepository notesRepository, UsersRepository usersRepository) {
        this.notesRepository = notesRepository;
        this.usersRepository = usersRepository;
    }

    @Transactional
    @Override
    public void setNoteForCoin(String email, String ticker, String noteText) {
        Optional<User> maybeUser = usersRepository.findByEmail(email);
        if (maybeUser.isEmpty()) throw new UserNotFoundException();
        User user = maybeUser.get();

        Optional<Note> maybeExistingNote = notesRepository.findByUserAndTicker(user, ticker);

        if (maybeExistingNote.isEmpty()) {
            createNewNoteForCoin(user, ticker, false, noteText);
            return;
        }

        Note existingNote = maybeExistingNote.get();
        boolean noteIsEmpty = noteText.equals("") && !existingNote.isFavorite();
        if (noteIsEmpty) {
            notesRepository.delete(existingNote);
        } else {
            existingNote.setNote(noteText);
            notesRepository.save(existingNote);
        }
    }

    @Transactional
    @Override
    public void setFavoriteForCoin(String email, String ticker, boolean isFavorite) {
        Optional<User> maybeUser = usersRepository.findByEmail(email);
        if (maybeUser.isEmpty()) throw new UserNotFoundException();
        User user = maybeUser.get();

        Optional<Note> maybeExistingNote = notesRepository.findByUserAndTicker(user, ticker);

        if (maybeExistingNote.isEmpty()) {
            createNewNoteForCoin(user, ticker, isFavorite, "");
            return;
        }

        Note existingNote = maybeExistingNote.get();
        boolean noteIsEmpty = !isFavorite && existingNote.getNote().equals("");
        if (noteIsEmpty) {
            notesRepository.delete(existingNote);
        } else {
            existingNote.setFavorite(isFavorite);
            notesRepository.save(existingNote);
        }
    }

    private void createNewNoteForCoin(User user, String ticker, boolean isFavorite, String noteText) {
        Note note = new Note();
        note.setTicker(ticker);
        note.setUser(user);
        note.setFavorite(isFavorite);
        note.setNote(noteText);
        notesRepository.save(note);
    }
}
