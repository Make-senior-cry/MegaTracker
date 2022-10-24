package ru.mirea.megatracker.scheduledTasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import ru.mirea.megatracker.api.coin.CoinInfo;
import ru.mirea.megatracker.api.coin.CoinPriceData;
import ru.mirea.megatracker.models.Coin;
import ru.mirea.megatracker.repositories.CoinsRepository;
import ru.mirea.megatracker.services.CoinAPIService;

@Component
public class FetchCoinsTask {

    private final CoinsRepository coinsRepository;
    private final CoinAPIService coinAPIService;
    private final Logger log = LoggerFactory.getLogger(FetchCoinsTask.class);

    public FetchCoinsTask(CoinsRepository coinsRepository, CoinAPIService coinAPIService) {
        this.coinsRepository = coinsRepository;
        this.coinAPIService = coinAPIService;
    }

    @Scheduled(fixedRate = 300000)
    public void manage() {
        log.info("Start fetching coins...");
        AtomicInteger updatedCoins = new AtomicInteger();
        for (int i = 0; i < 37; i++) {
            coinAPIService.fetchApiCoins(i).stream().filter(Objects::nonNull).forEach(apiCoin -> {
                Coin receivedCoin = new Coin();

                CoinInfo coinInfo = apiCoin.getCoinInfo();
                if(coinInfo != null) coinInfo.updateCoin(receivedCoin);

                CoinPriceData coinPriceData = apiCoin.getCoinPriceData();
                if(coinPriceData != null) coinPriceData.getPriceInfoUSD().updateCoin(receivedCoin);

                Optional<Coin> maybeCoin = coinsRepository.findByTicker(receivedCoin.getTicker());
                if (maybeCoin.isPresent()) receivedCoin = maybeCoin.get().updateFromModel(receivedCoin);

                coinsRepository.save(receivedCoin);
                updatedCoins.addAndGet(1);
            });
        }
        log.info("Finished fetching coins. Updated: " + updatedCoins.get());
    }
}
