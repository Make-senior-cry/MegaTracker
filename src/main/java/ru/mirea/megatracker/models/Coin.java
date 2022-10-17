package ru.mirea.megatracker.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.dto.coin.CoinInfoDTO;
import ru.mirea.megatracker.dto.coin.DetailedCoinInfoDTO;
import ru.mirea.megatracker.dto.coin.FavoriteCoinDTO;

import javax.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

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

    public CoinInfoDTO convertToCoinInfoDTO() {
        CoinInfoDTO dto = new CoinInfoDTO();
        BigDecimal transfer;

        dto.setName(name);
        dto.setTicker(ticker);
        dto.setIconUrl(iconUrl);
        dto.setCurrentPrice(currentPrice);
        transfer = new BigDecimal(deltaPrice);
        dto.setDeltaPrice(transfer.setScale(6, RoundingMode.HALF_UP).floatValue());
        transfer = new BigDecimal(deltaPricePercent);
        if (Math.abs(transfer.setScale(6, RoundingMode.HALF_UP).stripTrailingZeros().floatValue()) < 0.0000001) {
            dto.setDeltaPricePercent(0);
        } else {
            dto.setDeltaPricePercent(transfer.setScale(2, RoundingMode.HALF_UP).floatValue());
        }

        return dto;
    }

    public DetailedCoinInfoDTO convertToDetailedCoinInfoDTO(Optional<Note> maybeNote) {
        DetailedCoinInfoDTO dto = new DetailedCoinInfoDTO();

        dto.setName(name);
        dto.setTicker(ticker);
        dto.setCurrentPrice(currentPrice);
        dto.setNote("");

        BigDecimal transfer = new BigDecimal(deltaPrice);
        dto.setDeltaPrice(transfer.setScale(6, RoundingMode.HALF_UP).floatValue());
        transfer = new BigDecimal(deltaPricePercent);
        if (Math.abs(transfer.setScale(6, RoundingMode.HALF_UP).stripTrailingZeros().floatValue()) < 0.0000001) {
            dto.setDeltaPricePercent(0);
        } else {
            dto.setDeltaPricePercent(transfer.setScale(2, RoundingMode.HALF_UP).floatValue());
        }
        dto.setMarketCap(marketCap);
        dto.setHighDayPrice(highDayPrice);
        dto.setLowDayPrice(lowDayPrice);

        if (maybeNote.isPresent()) {
            Note note = maybeNote.get();
            dto.setNote(note.getNote());
            dto.setFavorite(note.isFavorite());
        }

        return dto;
    }

    public FavoriteCoinDTO convertToFavoriteCoinDTO(boolean isFavorite) {
        FavoriteCoinDTO dto = new FavoriteCoinDTO();

        dto.setName(name);
        dto.setTicker(ticker);
        dto.setIconUrl(iconUrl);
        dto.setCurrentPrice(currentPrice);
        BigDecimal transfer = new BigDecimal(deltaPrice);
        dto.setDeltaPrice(transfer.setScale(6, RoundingMode.HALF_UP).floatValue());
        transfer = new BigDecimal(deltaPricePercent);
        if (Math.abs(transfer.setScale(6, RoundingMode.HALF_UP).stripTrailingZeros().floatValue()) < 0.0000001) {
            dto.setDeltaPricePercent(0);
        } else {
            dto.setDeltaPricePercent(transfer.setScale(2, RoundingMode.HALF_UP).floatValue());
        }
        dto.setFavorite(isFavorite);
        return dto;
    }

    public Coin updateFromModel(Coin sourceModel) {
        setCurrentPrice(sourceModel.getCurrentPrice());
        setDeltaPrice(sourceModel.getDeltaPrice());
        setDeltaPricePercent(sourceModel.getDeltaPricePercent());
        setHighDayPrice(sourceModel.getHighDayPrice());
        setLowDayPrice(sourceModel.getLowDayPrice());
        setMarketCap(sourceModel.getMarketCap());
        return this;
    }
}
