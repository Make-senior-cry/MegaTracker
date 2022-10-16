package ru.mirea.megatracker.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.api.coin.ApiCoin;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopListApiResponse {

    @JsonProperty(value = "Data")
    private List<ApiCoin> data = new ArrayList<>();

    @JsonProperty(value = "Message")
    private String message;

}