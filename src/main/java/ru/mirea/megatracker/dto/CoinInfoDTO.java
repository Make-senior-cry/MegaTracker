package ru.mirea.megatracker.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CoinInfoDTO {
    private String name;
    private String ticker;
    private String iconUrl;
    private double currentPrice;
    private double deltaPrice;
    private double deltaPricePercent;
    private long marketCap;
    private double highDay;
    private double lowDay;
}
