package ru.mirea.megatracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtendedCoinDTO extends CoinDTO{

    private String userNote;

    private boolean isInFavourite;

    public ExtendedCoinDTO(){
        super();
    }

    public ExtendedCoinDTO(String name,
                           String ticker,
                           String imgURL,
                           double price,
                           double previousPriceDif,
                           String userNote,
                           boolean isInFavourite) {
        super(name, ticker, imgURL, price, previousPriceDif);
        this.userNote = userNote;
        this.isInFavourite = isInFavourite;
    }
}
