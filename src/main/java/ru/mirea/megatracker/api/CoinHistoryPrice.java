package ru.mirea.megatracker.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.dto.CoinPriceHistoryDTO;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CoinHistoryPrice {

    @JsonProperty(value = "time")
    private String time;

    @Setter
    @JsonProperty(value = " close")
    private long closePrice;

    public void setTime(long time) {
        this.time = convertUnixDateToString(time);
    }

    private String convertUnixDateToString(long unixDate){
        Date realDate = new Date(unixDate);
        return String.valueOf(realDate);
    }

    public void convertToDto(CoinPriceHistoryDTO dto, priceDif){
        dto.setClosingPrice(this.closePrice);
        dto.setTime(this.time);
        dto.setPriceDif(priceDif);
    }
}
