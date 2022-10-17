package ru.mirea.megatracker.api.coin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.dto.coin.CoinInfoDTO;
import ru.mirea.megatracker.dto.coin.DetailedCoinInfoDTO;
import ru.mirea.megatracker.models.Coin;

import java.math.BigDecimal;
import java.math.RoundingMode;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public abstract class PriceInfo {

    @JsonProperty(value = "PRICE")
    private BigDecimal currentPrice;

    @JsonProperty(value = "CHANGE24HOUR")
    private BigDecimal deltaPrice;

    @JsonProperty(value = "CHANGEPCT24HOUR")
    private BigDecimal deltaPricePercent;

    @JsonProperty(value = "MKTCAP")
    private long marketCap;

    @JsonProperty(value = "HIGHDAY")
    private BigDecimal highDay;

    @JsonProperty(value = "LOWDAY")
    private BigDecimal lowDay;

    public void updateCoin(Coin coin) {
        coin.setCurrentPrice(currentPrice.floatValue());
        coin.setDeltaPrice(deltaPrice.floatValue());
        coin.setDeltaPricePercent(deltaPricePercent.floatValue());
        coin.setDeltaPricePercent(deltaPricePercent.floatValue());
        coin.setMarketCap(marketCap);
        coin.setHighDayPrice(highDay.stripTrailingZeros().floatValue());
        coin.setLowDayPrice(lowDay.stripTrailingZeros().floatValue());
    }
}
