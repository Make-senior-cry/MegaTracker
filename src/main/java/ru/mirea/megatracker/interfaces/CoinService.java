package ru.mirea.megatracker.interfaces;

import java.util.List;
import java.util.Map;

import ru.mirea.megatracker.dto.coin.CoinPriceHistoryDTO;
import ru.mirea.megatracker.dto.coin.DetailedCoinInfoDTO;

public interface CoinService {
    Map<String, Object> getTopList(int page, int pageSize, float minPrice, float maxPrice, boolean isRising, String search);

    DetailedCoinInfoDTO getCoinByTicker(String email, String ticker);

    List<CoinPriceHistoryDTO> getPriceHistoryByTicker(String ticker);

    Map<String, Object> getFavoriteCoins(int page, int pageSize, String email);
}
