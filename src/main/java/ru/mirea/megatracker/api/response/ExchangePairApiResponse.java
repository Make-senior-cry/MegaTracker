package ru.mirea.megatracker.api.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.api.CoinInfo;
import ru.mirea.megatracker.api.PriceInfoUSD;

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

        @JsonProperty(value = "AggregatedData")
        private PriceInfoUSD priceInfoUSD;

    }
}
