package ru.mirea.megatracker.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CoinInfoDTO {
    private String name;
    private String imageUrl;
    private double price;
    private double changeDay;
    private double changeDayPct;
    private long marketCap;
    private double highDay;
    private double lowDay;
}
