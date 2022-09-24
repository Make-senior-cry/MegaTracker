package ru.mirea.megatracker.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.dto.CoinInfoDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public abstract class PriceInfo {

    @JsonProperty(value = "PRICE")
    private double price;

    @JsonProperty(value = "CHANGE24HOURS")
    private double changeDay;

    @JsonProperty(value = "CHANGEPCT24HOUR")
    private double changeDayPct;

    @JsonProperty(value = "MKTCAP")
    private long marketCap;

    @JsonProperty(value = "HIGHDAY")
    private double highDay;

    @JsonProperty(value = "LOWDAY")
    private double lowDay;

    public void convertToDTO(CoinInfoDTO coinInfoDTO) {
        coinInfoDTO.setPrice(price);
        coinInfoDTO.setChangeDay(changeDay);
        coinInfoDTO.setChangeDayPct(changeDayPct);
        coinInfoDTO.setMarketCap(marketCap);
        coinInfoDTO.setHighDay(highDay);
        coinInfoDTO.setLowDay(lowDay);
    }
}
