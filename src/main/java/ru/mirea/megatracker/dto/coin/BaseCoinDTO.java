package ru.mirea.megatracker.dto.coin;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class BaseCoinDTO {

    private String ticker;

    private String name;

    private float currentPrice;

    private float deltaPrice;

    private float deltaPricePercent;
}
