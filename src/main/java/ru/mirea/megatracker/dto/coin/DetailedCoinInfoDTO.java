package ru.mirea.megatracker.dto.coin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DetailedCoinInfoDTO extends BaseCoinDTO{
    @JsonProperty(value = "isFavorite")
    private boolean isFavorite;

    private String note;

    private float highDayPrice;

    private float lowDayPrice;

    private long marketCap;
}
