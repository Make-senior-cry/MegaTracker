package ru.mirea.megatracker.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.dto.coin.CoinInfoDTO;
import ru.mirea.megatracker.dto.coin.DetailedCoinInfoDTO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "coins")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Coin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "name")
    private String name;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "current_price")
    private float currentPrice;

    @Column(name = "delta_price")
    private float deltaPrice;

    @Column(name = "delta_price_percent")
    private float deltaPricePercent;

    @Column(name = "high_day_price")
    private float highDayPrice;

    @Column(name = "low_day_price")
    private float lowDayPrice;

    @Column(name = "market_cap")
    private long marketCap;

    public void convertToDTO(CoinInfoDTO coinInfoDTO) {
        BigDecimal transfer;

        coinInfoDTO.setName(name);
        coinInfoDTO.setTicker(ticker);
        coinInfoDTO.setIconUrl(iconUrl);
        coinInfoDTO.setCurrentPrice(currentPrice);
        transfer = new BigDecimal(deltaPrice);
        coinInfoDTO.setDeltaPrice(transfer.setScale(6, RoundingMode.HALF_UP).floatValue());
        transfer = new BigDecimal(deltaPricePercent);
        if (Math.abs(transfer.setScale(6, RoundingMode.HALF_UP).stripTrailingZeros().floatValue()) < 0.0000001) {
            coinInfoDTO.setDeltaPricePercent(0);
        }
        else {
            coinInfoDTO.setDeltaPricePercent(transfer.setScale(2, RoundingMode.HALF_UP).floatValue());
        }
    }

    public void convertToDTO(DetailedCoinInfoDTO detailedCoinInfoDTO) {
        BigDecimal transfer;

        detailedCoinInfoDTO.setName(name);
        detailedCoinInfoDTO.setTicker(ticker);
        detailedCoinInfoDTO.setCurrentPrice(currentPrice);
        detailedCoinInfoDTO.setNote("");
        transfer = new BigDecimal(deltaPrice);
        detailedCoinInfoDTO.setDeltaPrice(transfer.setScale(6, RoundingMode.HALF_UP).floatValue());
        transfer = new BigDecimal(deltaPricePercent);
        if (Math.abs(transfer.setScale(6, RoundingMode.HALF_UP).stripTrailingZeros().floatValue()) < 0.0000001) {
            detailedCoinInfoDTO.setDeltaPricePercent(0);
        }
        else {
            detailedCoinInfoDTO.setDeltaPricePercent(transfer.setScale(2, RoundingMode.HALF_UP).floatValue());
        }
        detailedCoinInfoDTO.setMarketCap(marketCap);
        detailedCoinInfoDTO.setHighDayPrice(highDayPrice);
        detailedCoinInfoDTO.setLowDayPrice(lowDayPrice);
    }
}
