package ru.mirea.megatracker.api.coin;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.dto.coin.CoinInfoDTO;
import ru.mirea.megatracker.dto.coin.DetailedCoinInfoDTO;
import ru.mirea.megatracker.models.Coin;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoinInfo {
    @JsonProperty(value = "FullName")
    private String name;

    @JsonProperty(value = "Name")
    private String ticker;

    @JsonProperty(value = "ImageUrl")
    private String iconUrl;

    public void updateCoin(Coin coin) {
        coin.setName(name);
        coin.setTicker(ticker);
        coin.setIconUrl("https://www.cryptocompare.com" + iconUrl);
    }
}
