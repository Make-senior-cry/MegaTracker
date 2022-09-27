package ru.mirea.megatracker.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.dto.CoinInfoDTO;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public abstract class PriceInfo {

    @JsonProperty(value = "PRICE")
    private double currentPrice;

    @JsonProperty(value = "CHANGE24HOUR")
    private double deltaPrice;

    @JsonProperty(value = "CHANGEPCT24HOUR")
    private double deltaPricePercent;

    @JsonProperty(value = "MKTCAP")
    private long marketCap;

    @JsonProperty(value = "HIGHDAY")
    private double highDay;

    @JsonProperty(value = "LOWDAY")
    private double lowDay;

    public void convertToDTO(CoinInfoDTO coinInfoDTO) {
        coinInfoDTO.setCurrentPrice(currentPrice);
        coinInfoDTO.setDeltaPrice(Math.round(deltaPrice * 100.0) / 100.0);
        coinInfoDTO.setDeltaPricePercent(deltaPricePercent);
        coinInfoDTO.setMarketCap(marketCap);
        coinInfoDTO.setHighDay(highDay);
        coinInfoDTO.setLowDay(lowDay);
    }
}
