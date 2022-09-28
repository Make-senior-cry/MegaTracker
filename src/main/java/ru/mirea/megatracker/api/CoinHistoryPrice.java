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
    @JsonProperty(value = "close")
    private double closePrice;

    public void setTime(long time) {
        this.time = convertUnixDateToString(time);
        //this.time = String.valueOf(time);
    }

    private String convertUnixDateToString(long unixDate){
        Date realDate = new Date(unixDate*1000L);
        return String.valueOf(realDate);
    }

    public void convertToDto(CoinPriceHistoryDTO dto, double priceDif){
        dto.setClosingPrice(this.closePrice);
        dto.setTime(this.time);
        dto.setPriceDif(priceDif);
    }

    @Override
    public String toString() {
        return "CoinHistoryPrice{" +
                "time='" + time + '\'' +
                ", closePrice=" + closePrice +
                '}';
    }
}