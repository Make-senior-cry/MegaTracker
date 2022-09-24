package ru.mirea.megatracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mirea.megatracker.api.ApiResponse;
import ru.mirea.megatracker.api.Coin;

import java.util.List;

@Service
public class CoinService {
    private final WebClient webClient;

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    public CoinService(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<Coin> getTopList(List<?> filters, int pageSize){
        ApiResponse list = webClient.get().
                uri(String.format("/data/top/totalvolfull?limit=%d&tsym=USD", pageSize)).
                header("Apikey {" + apiKey + "}").
                retrieve().bodyToMono(ApiResponse.class).block();
        return list.getData();
    }




}
