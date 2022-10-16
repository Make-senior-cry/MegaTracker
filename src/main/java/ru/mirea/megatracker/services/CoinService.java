package ru.mirea.megatracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import ru.mirea.megatracker.api.coin.CoinHistoryPrice;
import ru.mirea.megatracker.api.response.HistoryApiResponse;
import ru.mirea.megatracker.dto.coin.CoinInfoDTO;
import ru.mirea.megatracker.dto.coin.CoinPriceHistoryDTO;
import ru.mirea.megatracker.dto.coin.DetailedCoinInfoDTO;
import ru.mirea.megatracker.dto.coin.FavoriteCoinDTO;
import ru.mirea.megatracker.interfaces.ICoinAPIService;
import ru.mirea.megatracker.models.Coin;
import ru.mirea.megatracker.models.Note;
import ru.mirea.megatracker.models.User;
import ru.mirea.megatracker.repositories.CoinsRepository;
import ru.mirea.megatracker.repositories.NotesRepository;
import ru.mirea.megatracker.repositories.UsersRepository;
import ru.mirea.megatracker.util.CoinErrorResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CoinService implements ICoinAPIService {
    private final WebClient webClient;
    private final String apiKeyHeader;
    private final UsersRepository usersRepository;
    private final NotesRepository notesRepository;
    private final CoinsRepository coinsRepository;
    @Value("${api.key}")
    private String apiKey;

    @Autowired
    public CoinService(WebClient webClient, UsersRepository usersRepository, NotesRepository notesRepository, CoinsRepository coinsRepository) {
        this.webClient = webClient;
        this.usersRepository = usersRepository;
        this.notesRepository = notesRepository;
        this.coinsRepository = coinsRepository;
        this.apiKeyHeader = "Apikey {" + apiKey + "}";
    }

    @Override
    public Map<String, Object> getTopList(int page, int pageSize, float minPrice, float maxPrice, boolean isRising, String search) {
        Map<String, Object> response = new HashMap<>();
        List<Coin> filteredCoins;
        List<Coin> searchedCoins = new ArrayList<>();
        List<Coin> coins;
        if (!search.equals("")) {
            searchedCoins = coinsRepository.findAllBySearch(search.toLowerCase(), search.toLowerCase());
        }

        if (isRising) {
            filteredCoins = coinsRepository.findAllRisingWithFilters(minPrice, maxPrice);
        } else {
            filteredCoins = coinsRepository.findAllWithFilters(minPrice, maxPrice);
        }

        if (!searchedCoins.isEmpty()) {
            coins = filteredCoins.stream().filter(searchedCoins::contains).collect(Collectors.toList());
        } else {
            coins = filteredCoins;
        }

        response.put("pageCount", ((coins.size() - 1) / pageSize) + 1);
        List<CoinInfoDTO> arrayResponse = new ArrayList<>(pageSize);
        for (int i = (page - 1) * pageSize; i < page * pageSize; i++) {
            if (i > coins.size() - 1) break;
            CoinInfoDTO coinInfoDTO = new CoinInfoDTO();
            coins.get(i).convertToDTO(coinInfoDTO);
            arrayResponse.add(coinInfoDTO);
        }
        response.put("coins", arrayResponse);

        return response;
    }

    @Override
    public DetailedCoinInfoDTO getCoinByTicker(String email, String ticker) {
        DetailedCoinInfoDTO response = new DetailedCoinInfoDTO();
        Coin coin = coinsRepository.findByTicker(ticker);
        coin.convertToDTO(response);

        Optional<User> user = usersRepository.findByEmail(email);
        Optional<Note> note = notesRepository.findByUserAndTicker(user.get(), ticker);

        if (note.isPresent()) {
            response.setNote(note.get().getNote());
            response.setFavorite(note.get().isFavorite());
        }
        return response;
    }

    @Override
    public List<CoinPriceHistoryDTO> getPriceHistoryByTicker(String ticker) {

        HistoryApiResponse historyApiResponse = webClient.get()
                .uri(String.format("/v2/histoday?fsym=%s&tsym=USD&limit=30", ticker)).header(apiKeyHeader).retrieve()
                .bodyToMono(HistoryApiResponse.class).block();
        if (historyApiResponse == null || !historyApiResponse.getMessage().equals("Success")) {
            throw new CoinErrorResponse("Failed to get price history");
        }
        int count = 31;
        List<CoinPriceHistoryDTO> response = new ArrayList<>(count);
        List<CoinHistoryPrice> historyList = historyApiResponse.getData().getCoinHistoryPrice();
        for (int i = 1; i < count; i++) {
            CoinPriceHistoryDTO coinPriceHistoryDTO = new CoinPriceHistoryDTO();
            BigDecimal curr = historyList.get(i).getClosingPrice();
            BigDecimal prev = historyList.get(i - 1).getClosingPrice();
            float deltaPrice = curr.subtract(prev).setScale(Math.max(prev.scale(), curr.scale()), RoundingMode.HALF_UP)
                    .stripTrailingZeros().floatValue();
            historyList.get(i).convertToDto(coinPriceHistoryDTO, deltaPrice);
            response.add(coinPriceHistoryDTO);
        }
        return response;
    }

    @Override
    public Map<String, Object> getFavoriteCoins(int page, int pageSize, String email) {
        Map<String, Object> request = new HashMap<>();

        Optional<User> user = usersRepository.findByEmail(email);
        List<Note> notes;
        List<Coin> favoriteCoins = new ArrayList<>();
        List<FavoriteCoinDTO> coins = new ArrayList<>();

        if (user.isPresent()) {
            notes = notesRepository.findAllByUserAndIsFavorite(user.get(), true);
            for (int i = 0; i < notes.size(); i++) {
                favoriteCoins.add(coinsRepository.findByTicker(notes.get(i).getTicker()));
            }

            request.put("pageCount", ((favoriteCoins.size() - 1) / pageSize) + 1);
            Collections.reverse(favoriteCoins);

            for (int i = (page * pageSize) - pageSize; i < page * pageSize; i++) {
                FavoriteCoinDTO favoriteCoinDTO = new FavoriteCoinDTO();
                if (i < favoriteCoins.size()) {
                    favoriteCoins.get(i).convertToDTO(favoriteCoinDTO);
                    favoriteCoinDTO.setFavorite(
                            notesRepository.findByUserAndTicker(user.get(), favoriteCoinDTO.getTicker()).get()
                                    .isFavorite());
                    coins.add(favoriteCoinDTO);
                } else {
                    break;
                }
            }
        }
        request.put("coins", coins);
        return request;
    }
}
