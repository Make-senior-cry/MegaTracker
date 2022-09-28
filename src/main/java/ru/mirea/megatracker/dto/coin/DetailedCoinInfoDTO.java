package ru.mirea.megatracker.dto.coin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DetailedCoinInfoDTO extends BaseCoinDTO{

    private boolean isFavorite;
    private String note;

    private float highDayPrice;

    private float lowDayPrice;

    private long marketCap;
}
