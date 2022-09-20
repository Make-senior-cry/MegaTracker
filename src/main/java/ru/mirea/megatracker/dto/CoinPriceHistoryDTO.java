package ru.mirea.megatracker.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CoinPriceHistoryDTO {
    private String ticker;

    private List<Double> priceList;
}
