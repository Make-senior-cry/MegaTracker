package ru.mirea.megatracker.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import ru.mirea.megatracker.api.coin.ApiCoin;
import ru.mirea.megatracker.api.response.TopListApiResponse;

import ru.mirea.megatracker.models.Coin;
import ru.mirea.megatracker.repositories.CoinsRepository;

import java.util.List;
import java.util.Optional;

@Component
public class EventManager {

    private final String apiKeyHeader;
    private final WebClient webClient;
    private final CoinsRepository coinsRepository;
    @Value("${api.key}")
    private String apiKey;

    @Autowired
    public EventManager(WebClient webClient, CoinsRepository coinsRepository) {
        this.apiKeyHeader = "Apikey {" + apiKey + "}";
        this.webClient = webClient;
        this.coinsRepository = coinsRepository;
    }

    @Scheduled(fixedRate = 300000)
    public void manage() {
        for (int i = 0; i < 37; i++) {
            TopListApiResponse topListApiResponse = webClient.get()
                    .uri(String.format("top/totalvolfull?limit=%d&tsym=USD&page=%d", 88, i)).header(apiKeyHeader)
                    .retrieve().bodyToMono(TopListApiResponse.class).block();
            if (topListApiResponse == null || !topListApiResponse.getMessage().equals("Success")) {
                throw new CoinErrorResponse("Coins error");
            }
            List<ApiCoin> apiCoins = topListApiResponse.getData();
            for (ApiCoin apiCoin : apiCoins) {
                if (apiCoin.getCoinPriceData() == null) {
                    continue;
                }
                Coin receivedCoin = new Coin();
                apiCoin.getCoinInfo().convertToModel(receivedCoin);
                apiCoin.getCoinPriceData().getPriceInfoUSD().convertToModel(receivedCoin);
                Optional<Coin> maybeCoin = coinsRepository.findByTicker(receivedCoin.getTicker());
                if (maybeCoin.isPresent()) {
                    Coin coin = maybeCoin.get();
                    coin.setCurrentPrice(receivedCoin.getCurrentPrice());
                    coin.setDeltaPrice(receivedCoin.getDeltaPrice());
                    coin.setDeltaPricePercent(receivedCoin.getDeltaPricePercent());
                    coin.setHighDayPrice(receivedCoin.getHighDayPrice());
                    coin.setLowDayPrice(receivedCoin.getLowDayPrice());
                    coin.setMarketCap(receivedCoin.getMarketCap());
                    coinsRepository.save(coin);
                } else {
                    coinsRepository.save(receivedCoin);
                }
            }
        }
    }
}
