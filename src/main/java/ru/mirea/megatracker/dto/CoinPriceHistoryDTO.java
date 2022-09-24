package ru.mirea.megatracker.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CoinPriceHistoryDTO {
    private double closingPrice;
    private double priceDif;
    private long time;
}
