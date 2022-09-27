package ru.mirea.megatracker.api;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.dto.CoinInfoDTO;
import ru.mirea.megatracker.dto.DetailedCoinInfoDTO;

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

    public void convertToDTO(CoinInfoDTO coinInfoDTO) {
        coinInfoDTO.setName(name);
        coinInfoDTO.setTicker(ticker);
        coinInfoDTO.setIconUrl("https://www.cryptocompare.com" + iconUrl);
    }

    public void convertToDTO(DetailedCoinInfoDTO detailedCoinInfoDTO) {
        detailedCoinInfoDTO.setName(name);
        detailedCoinInfoDTO.setTicker(ticker);
        detailedCoinInfoDTO.setImageUrl("https://www.cryptocompare.com" + iconUrl);
    }
}
