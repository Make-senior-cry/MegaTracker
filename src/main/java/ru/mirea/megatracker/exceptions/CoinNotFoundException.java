package ru.mirea.megatracker.exceptions;

public class CoinNotFoundException extends RuntimeException{
    public CoinNotFoundException(String ticker) {
        super(String.format("Coin with ticker %s was not found", ticker));
    }
}
