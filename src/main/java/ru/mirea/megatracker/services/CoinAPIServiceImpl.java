package ru.mirea.megatracker.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import ru.mirea.megatracker.api.coin.ApiCoin;
import ru.mirea.megatracker.api.coin.CoinHistoryPrice;
import ru.mirea.megatracker.api.response.HistoryApiResponse;
import ru.mirea.megatracker.api.response.TopListApiResponse;
import ru.mirea.megatracker.interfaces.CoinAPIService;
import ru.mirea.megatracker.exceptions.CoinFetchFailedException;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class CoinAPIServiceImpl implements CoinAPIService {
    private final WebClient webClient;
    @Value("${api.key}")
    private String APIKey;

    public CoinAPIServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    private String getAPIHeader() {
        return String.format("Apikey {%s}", APIKey);
    }

    @Override
    public List<CoinHistoryPrice> fetchHistoryPrice(String ticker) {
        String uri = String.format("/v2/histoday?fsym=%s&tsym=USD&limit=30", ticker);
        HistoryApiResponse historyApiResponse = webClient.get().uri(uri).header(getAPIHeader()).retrieve()
                .bodyToMono(HistoryApiResponse.class).block();

        boolean requestFailed = historyApiResponse == null || !historyApiResponse.getMessage().equals("Success");
        if (requestFailed) throw new CoinFetchFailedException("Failed to get price history");

        return historyApiResponse.getData().getCoinHistoryPrice();
    }

    @Override
    public List<ApiCoin> fetchApiCoins(int page) {
        String uri = String.format("top/totalvolfull?limit=%d&tsym=USD&page=%d", 88, page);
        TopListApiResponse topListApiResponse = webClient.get().uri(uri).header(getAPIHeader())
                .retrieve().bodyToMono(TopListApiResponse.class).block();

        boolean requestFailed = topListApiResponse == null || !topListApiResponse.getMessage().equals("Success");
        if (requestFailed) throw new CoinFetchFailedException("Coins error");

        return topListApiResponse.getData();
    }
}
