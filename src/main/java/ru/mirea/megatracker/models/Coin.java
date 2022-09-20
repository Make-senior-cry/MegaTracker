package ru.mirea.megatracker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.API.CoinInfo;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coin {

   /* @JsonProperty(value = "Name")
    private String name;

    @JsonProperty(value = "FullName")
    private String fullName;
*/
    @JsonProperty(value = "CoinInfo")
    private CoinInfo coinInfo;
}
