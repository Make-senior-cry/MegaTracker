package ru.mirea.megatracker.interfaces;

public interface NoteService {
    public void setNoteForCoin(String email, String ticker, String newNote);
    public void setFavoriteForCoin(String email, String ticker, boolean isFavorite);
}
