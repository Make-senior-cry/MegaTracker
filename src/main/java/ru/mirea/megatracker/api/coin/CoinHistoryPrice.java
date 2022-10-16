package ru.mirea.megatracker.api.coin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.dto.coin.CoinPriceHistoryDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CoinHistoryPrice {

    @JsonProperty(value = "time")
    private long time;

    @Setter
    @JsonProperty(value = "close")
    private BigDecimal closingPrice;

    public void setTime(long time) {
        this.time = time * 1000L;
    }

    public CoinPriceHistoryDTO convertToCoinPriceHistoryDTO(float deltaPrice) {
        CoinPriceHistoryDTO dto = new CoinPriceHistoryDTO();
        dto.setClosingPrice(closingPrice.floatValue());
        dto.setDateTime(time);
        dto.setDeltaClosingPrice(deltaPrice);
        dto.setDeltaClosingPricePercent(new BigDecimal(deltaPrice/closingPrice.floatValue()*100)
                .setScale(closingPrice.scale(), RoundingMode.HALF_UP)
                .floatValue());
        return dto;
    }

    @Override
    public String toString() {
        return "CoinHistoryPrice{" +
                "dateTime='" + time + '\'' +
                ", closingPrice=" + closingPrice +
                '}';
    }
}
