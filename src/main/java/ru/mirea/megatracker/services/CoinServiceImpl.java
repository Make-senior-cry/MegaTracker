package ru.mirea.megatracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import ru.mirea.megatracker.api.coin.CoinHistoryPrice;
import ru.mirea.megatracker.dto.coin.CoinInfoDTO;
import ru.mirea.megatracker.dto.coin.CoinPriceHistoryDTO;
import ru.mirea.megatracker.dto.coin.DetailedCoinInfoDTO;
import ru.mirea.megatracker.dto.coin.FavoriteCoinDTO;
import ru.mirea.megatracker.exceptions.CoinNotFoundException;
import ru.mirea.megatracker.exceptions.UserNotAuthenticatedException;
import ru.mirea.megatracker.exceptions.UserNotFoundException;
import ru.mirea.megatracker.interfaces.CoinAPIService;
import ru.mirea.megatracker.interfaces.CoinService;
import ru.mirea.megatracker.models.Coin;
import ru.mirea.megatracker.models.Note;
import ru.mirea.megatracker.models.User;
import ru.mirea.megatracker.repositories.CoinsRepository;
import ru.mirea.megatracker.repositories.NotesRepository;
import ru.mirea.megatracker.repositories.UsersRepository;

@Service
public class CoinServiceImpl implements CoinService {
    private final UsersRepository usersRepository;
    private final NotesRepository notesRepository;
    private final CoinsRepository coinsRepository;
    private final CoinAPIService coinAPIService;

    @Autowired
    public CoinServiceImpl(UsersRepository usersRepository, NotesRepository notesRepository, CoinsRepository coinsRepository, CoinAPIService coinAPIService) {
        this.usersRepository = usersRepository;
        this.notesRepository = notesRepository;
        this.coinsRepository = coinsRepository;
        this.coinAPIService = coinAPIService;
    }

    @Override
    public Map<String, Object> getTopList(int page, int pageSize, float minPrice, float maxPrice, boolean isRising, String search) {
        float minDeltaPrice = isRising ? (float) 0.001 : -Float.MAX_VALUE;
        List<Coin> coins = coinsRepository.findAllWithFilters(minPrice, maxPrice, minDeltaPrice);
        if (search.length() > 0) {
            coins = coins.stream().filter(coin -> coin.getName().toLowerCase().contains(search)).toList();
        }

        List<CoinInfoDTO> paginatedCoins = new ArrayList<>(pageSize);
        for (int i = (page - 1) * pageSize; (i < page * pageSize) && (i <= coins.size() - 1); i++) {
            Coin coin = coins.get(i);
            paginatedCoins.add(coin.convertToCoinInfoDTO());
        }

        return createPaginatedCoinsResponse(paginatedCoins, getPageCount(coins, pageSize));
    }

    @Override
    public DetailedCoinInfoDTO getCoinByTicker(String email, String ticker) {
        Optional<Coin> maybeCoin = coinsRepository.findByTicker(ticker);
        if (maybeCoin.isEmpty()) throw new CoinNotFoundException(ticker);

        Optional<User> maybeUser = usersRepository.findByEmail(email);
        if (maybeUser.isEmpty()) throw new UserNotFoundException();

        Optional<Note> maybeNote = notesRepository.findByUserAndTicker(maybeUser.get(), ticker);

        return maybeCoin.get().convertToDetailedCoinInfoDTO(maybeNote);
    }

    @Override
    public List<CoinPriceHistoryDTO> getPriceHistoryByTicker(String ticker) {
        List<CoinHistoryPrice> historyList = coinAPIService.fetchHistoryPrice(ticker);

        final int count = 31;
        List<CoinPriceHistoryDTO> priceHistory = new ArrayList<>(count);
        for (int i = 1; i < count; i++) {
            BigDecimal curr = historyList.get(i).getClosingPrice();
            BigDecimal prev = historyList.get(i - 1).getClosingPrice();
            float deltaPrice = curr.subtract(prev).setScale(Math.max(prev.scale(), curr.scale()), RoundingMode.HALF_UP)
                    .stripTrailingZeros().floatValue();

            CoinHistoryPrice coinHistoryPrice = historyList.get(i);
            priceHistory.add(coinHistoryPrice.convertToCoinPriceHistoryDTO(deltaPrice));
        }

        return priceHistory;
    }

    @Override
    public Map<String, Object> getFavoriteCoins(int page, int pageSize, String email) {
        Optional<User> user = usersRepository.findByEmail(email);
        // TODO: fix not authenticated exception
        if (user.isEmpty()) throw new UserNotAuthenticatedException();

        ArrayList<Coin> favoriteCoins = notesRepository.findAllByUserAndIsFavorite(user.get(), true).stream()
                .map(note -> coinsRepository.findByTicker(note.getTicker()).orElse(null)).filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.reverse(favoriteCoins);

        List<FavoriteCoinDTO> paginatedCoins = new ArrayList<>();
        for (int i = (page * pageSize) - pageSize; (i < page * pageSize) && (i < favoriteCoins.size()); i++) {
            Coin coin = favoriteCoins.get(i);
            paginatedCoins.add(coin.convertToFavoriteCoinDTO(true));
        }

        return createPaginatedCoinsResponse(paginatedCoins, getPageCount(favoriteCoins, pageSize));
    }

    private int getPageCount(List<?> entities, int pageSize) {
        return ((entities.size() - 1) / pageSize) + 1;
    }

    private Map<String, Object> createPaginatedCoinsResponse(List<?> coins, int pageCount) {
        Map<String, Object> response = new HashMap<>();
        response.put("pageCount", pageCount);
        response.put("coins", coins);
        return response;
    }
}
