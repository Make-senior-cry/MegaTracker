package ru.mirea.megatracker.dto.coin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CoinPriceHistoryDTO {
    private float closingPrice;
    private float deltaClosingPrice;
    private long dateTime;
}
