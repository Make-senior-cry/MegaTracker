package ru.mirea.megatracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinDTO {

    private String name;

    private String ticker;

    private String imgURL;

    private double price;

    private double previousPriceDif;

    public CoinDTO(String name,
                   String ticker,
                   String imgURL,
                   double price,
                   double previousPriceDif) {
        this.name = name;
        this.ticker = ticker;
        this.imgURL = imgURL;
        this.price = price;
        this.previousPriceDif = previousPriceDif;
    }
}
