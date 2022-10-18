package ru.mirea.megatracker.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoinFetchFailedException extends RuntimeException {
    private String message;

    public CoinFetchFailedException(String message) {
        this.message = message;
    }
}
