package ru.mirea.megatracker.api;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.dto.CoinInfoDTO;

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
    private String imageUrl;

    public void convertToDTO(CoinInfoDTO coinInfoDTO) {
        coinInfoDTO.setName(name);
        coinInfoDTO.setTicker(ticker);
        coinInfoDTO.setImageUrl(imageUrl);
    }
}
