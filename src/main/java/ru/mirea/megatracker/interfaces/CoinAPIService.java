package ru.mirea.megatracker.interfaces;

import java.util.List;

import ru.mirea.megatracker.api.coin.CoinHistoryPrice;

public interface CoinAPIService {
    List<CoinHistoryPrice> fetchHistoryPrice(String ticker);
}
