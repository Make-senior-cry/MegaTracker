package ru.mirea.megatracker.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.dto.coin.CoinPriceHistoryDTO;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
    private BigDecimal closingPrice;

    public void setTime(long time) {
        this.time = convertUnixDateToString(time);
        //this.dateTime = String.valueOf(dateTime);
    }

    private String convertUnixDateToString(long unixDate){
        Date realDate = new Date(unixDate*1000L);
        SimpleDateFormat res = new SimpleDateFormat("dd/MM/yyyy");
        return res.format(realDate);
    }

    public void convertToDto(CoinPriceHistoryDTO dto, float deltaPrice){
        dto.setClosingPrice(closingPrice.floatValue());
        dto.setDateTime(time);
        dto.setDeltaClosingPrice(deltaPrice);
    }

    @Override
    public String toString() {
        return "CoinHistoryPrice{" +
                "dateTime='" + time + '\'' +
                ", closingPrice=" + closingPrice +
                '}';
    }
}
