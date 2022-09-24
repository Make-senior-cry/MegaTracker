package ru.mirea.megatracker.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class DetailedCoinInfoDTO {
    private String name;
    private String ticker;
    private String imageUrl;
    private double price;
    private boolean isFavorite;
    private String note;
    private long timeFrom;
    private long timeTo;
    private List<CoinPriceHistoryDTO> priceHistory;
}
