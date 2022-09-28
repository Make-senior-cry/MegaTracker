package ru.mirea.megatracker.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.dto.coin.CoinInfoDTO;
import ru.mirea.megatracker.dto.coin.DetailedCoinInfoDTO;

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
        coinInfoDTO.setDeltaPrice(deltaPrice.setScale(Math.max(highDay.scale(), Math.max(lowDay.scale(), currentPrice.scale())),
                RoundingMode.HALF_UP).stripTrailingZeros().floatValue());
        coinInfoDTO.setDeltaPricePercent(deltaPricePercent.setScale(Math.max(highDay.scale(), Math.max(lowDay.scale(), currentPrice.scale())),
                RoundingMode.HALF_UP).stripTrailingZeros().floatValue());
    }

    public void convertToDTO(DetailedCoinInfoDTO detailedCoinInfoDTO){
        detailedCoinInfoDTO.setCurrentPrice(currentPrice.floatValue());
        detailedCoinInfoDTO.setDeltaPrice(deltaPrice.setScale(Math.max(highDay.scale(), Math.max(lowDay.scale(), currentPrice.scale())),
                RoundingMode.HALF_UP).stripTrailingZeros().floatValue());
        detailedCoinInfoDTO.setDeltaPricePercent(deltaPricePercent.setScale(Math.max(highDay.scale(), Math.max(lowDay.scale(), currentPrice.scale())),
                RoundingMode.HALF_UP).stripTrailingZeros().floatValue());
        detailedCoinInfoDTO.setMarketCap(marketCap);
        detailedCoinInfoDTO.setHighDayPrice(highDay.stripTrailingZeros().floatValue());
        detailedCoinInfoDTO.setLowDayPrice(lowDay.stripTrailingZeros().floatValue());
    }
}
