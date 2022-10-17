package ru.mirea.megatracker.scheduledTasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import ru.mirea.megatracker.interfaces.CoinAPIService;
import ru.mirea.megatracker.models.Coin;
import ru.mirea.megatracker.repositories.CoinsRepository;

@Component
public class FetchCoinsTask {

    private final CoinsRepository coinsRepository;
    private CoinAPIService coinAPIService;
    private final int PAGES_COUNT = 37;

    public FetchCoinsTask(CoinsRepository coinsRepository, CoinAPIService coinAPIService) {
        this.coinsRepository = coinsRepository;
        this.coinAPIService = coinAPIService;
    }

    @Scheduled(fixedRate = 300000)
    public void manage() {
        for (int i = 0; i < PAGES_COUNT; i++) {
            coinAPIService.fetchApiCoins(i).stream().filter(Objects::nonNull).forEach(apiCoin -> {
                Coin receivedCoin = new Coin();
                apiCoin.getCoinInfo().convertToModel(receivedCoin);
                apiCoin.getCoinPriceData().getPriceInfoUSD().convertToModel(receivedCoin);
                Optional<Coin> maybeCoin = coinsRepository.findByTicker(receivedCoin.getTicker());

                if (maybeCoin.isPresent()) receivedCoin = maybeCoin.get().updateFromModel(receivedCoin);

                coinsRepository.save(receivedCoin);
            });
        }
    }
}
