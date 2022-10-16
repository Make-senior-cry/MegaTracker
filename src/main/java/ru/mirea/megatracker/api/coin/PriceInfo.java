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

    public void convertToDTO(CoinInfoDTO coinInfoDTO) {
        coinInfoDTO.setCurrentPrice(currentPrice.floatValue());
        coinInfoDTO.setDeltaPrice(deltaPrice.setScale(6, RoundingMode.HALF_UP)
                .stripTrailingZeros().floatValue());
        if (Math.abs(deltaPrice.setScale(6, RoundingMode.HALF_UP).stripTrailingZeros().floatValue()) < 0.0000001) {
            coinInfoDTO.setDeltaPricePercent(0);
        } else {
            coinInfoDTO.setDeltaPricePercent(deltaPricePercent.setScale(2, RoundingMode.HALF_UP).floatValue());
        }

    }

    public void convertToDTO(DetailedCoinInfoDTO detailedCoinInfoDTO) {
        detailedCoinInfoDTO.setCurrentPrice(currentPrice.floatValue());
        detailedCoinInfoDTO.setDeltaPrice(deltaPrice.setScale(6, RoundingMode.HALF_UP).floatValue());
        if (Math.abs(deltaPrice.setScale(6, RoundingMode.HALF_UP).stripTrailingZeros().floatValue()) < 0.0000001) {
            detailedCoinInfoDTO.setDeltaPricePercent(0);
        } else {
            detailedCoinInfoDTO.setDeltaPricePercent(deltaPricePercent.setScale(2, RoundingMode.HALF_UP).floatValue());
        }
        detailedCoinInfoDTO.setDeltaPricePercent(deltaPricePercent.setScale(2, RoundingMode.HALF_UP).floatValue());
        detailedCoinInfoDTO.setMarketCap(marketCap);
        detailedCoinInfoDTO.setHighDayPrice(highDay.stripTrailingZeros().floatValue());
        detailedCoinInfoDTO.setLowDayPrice(lowDay.stripTrailingZeros().floatValue());
    }

    public void convertToModel(Coin coin) {
        coin.setCurrentPrice(currentPrice.floatValue());
        coin.setDeltaPrice(deltaPrice.floatValue());
        coin.setDeltaPricePercent(deltaPricePercent.floatValue());
        coin.setDeltaPricePercent(deltaPricePercent.floatValue());
        coin.setMarketCap(marketCap);
        coin.setHighDayPrice(highDay.stripTrailingZeros().floatValue());
        coin.setLowDayPrice(lowDay.stripTrailingZeros().floatValue());
    }
}
