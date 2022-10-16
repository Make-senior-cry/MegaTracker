package ru.mirea.megatracker.interfaces;

import java.util.List;
import java.util.Map;

import ru.mirea.megatracker.dto.coin.CoinPriceHistoryDTO;
import ru.mirea.megatracker.dto.coin.DetailedCoinInfoDTO;

public interface CoinAPIService {
    public Map<String, Object> getTopList(int page, int pageSize, float minPrice, float maxPrice, boolean isRising, String search);

    public DetailedCoinInfoDTO getCoinByTicker(String email, String ticker);

    public List<CoinPriceHistoryDTO> getPriceHistoryByTicker(String ticker);

    public Map<String, Object> getFavoriteCoins(int page, int pageSize, String email);
}
