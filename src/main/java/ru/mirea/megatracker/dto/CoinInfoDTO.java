package ru.mirea.megatracker.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class CoinInfoDTO {
    private String name;
    private String ticker;
    private String iconUrl;
    private float currentPrice;
    private float deltaPrice;
    private float deltaPricePercent;
    private long marketCap;
    private float highDay;
    private float lowDay;
}
