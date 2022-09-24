package ru.mirea.megatracker.API;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.API.CoinInfo;
import ru.mirea.megatracker.API.CoinPriceData;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coin {

    @JsonProperty(value = "RAW")
    private CoinPriceData coinPriceData;

    @JsonProperty(value = "CoinInfo")
    private CoinInfo coinInfo;
}
