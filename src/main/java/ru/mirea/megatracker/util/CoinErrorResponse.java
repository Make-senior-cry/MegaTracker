package ru.mirea.megatracker.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoinErrorResponse extends RuntimeException {
    private String message;

    public CoinErrorResponse(String message) {
        this.message = message;
    }
}
