package ru.mirea.megatracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mirea.megatracker.API.ApiResponse;
import ru.mirea.megatracker.dto.Coin;

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




}
