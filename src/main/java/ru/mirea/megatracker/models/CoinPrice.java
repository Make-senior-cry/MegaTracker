package ru.mirea.megatracker.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
public class CoinPrice {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String date;

    private double priceAtDate;

    private double priceDiff;
}
