package ru.mirea.megatracker.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.dto.coin.CoinPriceHistoryDTO;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CoinHistoryPrice {

    @JsonProperty(value = "dateTime")
    private String time;

    @Setter
    @JsonProperty(value = "close")
    private double closePrice;

    public void setTime(long time) {
        this.time = convertUnixDateToString(time);
        //this.dateTime = String.valueOf(dateTime);
    }

    private String convertUnixDateToString(long unixDate){
        Date realDate = new Date(unixDate*1000L);
        return String.valueOf(realDate);
    }

    public void convertToDto(CoinPriceHistoryDTO dto, double priceDif){
        dto.setClosingPrice(this.closePrice);
        dto.setDateTime(this.time);
        dto.setDeltaClosingPrice(priceDif);
    }

    @Override
    public String toString() {
        return "CoinHistoryPrice{" +
                "dateTime='" + time + '\'' +
                ", closePrice=" + closePrice +
                '}';
    }
}
