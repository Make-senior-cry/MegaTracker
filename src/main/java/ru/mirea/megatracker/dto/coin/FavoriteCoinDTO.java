package ru.mirea.megatracker.dto.coin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FavoriteCoinDTO extends CoinInfoDTO {
    @JsonProperty(value = "isFavorite")
    private boolean isFavorite;
}
