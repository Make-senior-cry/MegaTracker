package ru.mirea.megatracker.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.mirea.megatracker.API.ApiResponse;
import ru.mirea.megatracker.models.Coin;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoinService {
    //TODO

    private final WebClient webClient;

    @Autowired
    public CoinService(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<Coin> getTopList(List<?> filters, int cursor, int pageSize){
        ApiResponse list = webClient.get().
                uri(String.format("/data/top/totalvolfull?limit=%d&tsym=USD", pageSize)).
                header("Apikey {cb0b9f6468d47f38977dc89ec588774b38b9715028f2c91ef2969dfd70391cab}").
                retrieve().bodyToMono(ApiResponse.class).block();

        return list.getData();
    }

    public Coin getPrice(){
        Coin pr = webClient.get().
                uri("/data/price?fsym=BTC&tsyms=USD")
                .header("Apikey {cb0b9f6468d47f38977dc89ec588774b38b9715028f2c91ef2969dfd70391cab}")
                .retrieve().bodyToMono(Coin.class).block();
        return pr;
    }


}
