package ru.mirea.megatracker.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.dto.coin.CoinInfoDTO;

import javax.persistence.*;

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
        coinInfoDTO.setName(name);
        coinInfoDTO.setTicker(ticker);
        coinInfoDTO.setIconUrl(iconUrl);
        coinInfoDTO.setCurrentPrice(currentPrice);
        coinInfoDTO.setDeltaPrice(deltaPrice);
        coinInfoDTO.setDeltaPricePercent(deltaPricePercent);
    }
}
