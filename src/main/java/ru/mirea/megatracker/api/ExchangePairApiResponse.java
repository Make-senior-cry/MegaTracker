package ru.mirea.megatracker.api;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangePairApiResponse {

    @JsonProperty(value = "Response")
    private String message;

    @JsonProperty(value = "Data")
    private Data data;


    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {

        @JsonProperty(value = "CoinInfo")
        private CoinInfo coinInfo;

    }
}
