package ru.mirea.megatracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mirea.megatracker.api.coin.ApiCoin;
import ru.mirea.megatracker.api.coin.CoinHistoryPrice;
import ru.mirea.megatracker.api.coin.CoinInfo;
import ru.mirea.megatracker.api.coin.CoinPriceData;
import ru.mirea.megatracker.api.response.ExchangePairApiResponse;
import ru.mirea.megatracker.api.response.HistoryApiResponse;
import ru.mirea.megatracker.api.response.TopListApiResponse;
import ru.mirea.megatracker.dto.coin.CoinInfoDTO;
import ru.mirea.megatracker.dto.coin.CoinPriceHistoryDTO;
import ru.mirea.megatracker.dto.coin.DetailedCoinInfoDTO;
import ru.mirea.megatracker.models.Coin;
import ru.mirea.megatracker.models.Note;
import ru.mirea.megatracker.models.User;
import ru.mirea.megatracker.repositories.CoinsRepository;
import ru.mirea.megatracker.repositories.NotesRepository;
import ru.mirea.megatracker.repositories.UsersRepository;
import ru.mirea.megatracker.util.CoinErrorResponse;
import ru.mirea.megatracker.util.Filter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class CoinService {
    private final WebClient webClient;

    @Value("${api.key}")
    private String apiKey;

    private final String apiKeyHeader;
    private final UsersRepository usersRepository;
    private final NotesRepository notesRepository;
    private final CoinsRepository coinsRepository;

    @Autowired
    public CoinService(WebClient webClient, UsersRepository usersRepository, NotesRepository notesRepository, CoinsRepository coinsRepository) {
        this.webClient = webClient;
        this.usersRepository = usersRepository;
        this.notesRepository = notesRepository;
        this.coinsRepository = coinsRepository;
        this.apiKeyHeader = "Apikey {" + apiKey + "}";
    }

    public Map<Object, Object> getTopList(int page, int pageSize) throws CoinErrorResponse {
        Map<Object, Object> response = new HashMap<>();
        response.put("pageCount", (coinsRepository.count() / pageSize));

        List<CoinInfoDTO> arrayResponse = new ArrayList<>(pageSize);
        for (int i = (page * pageSize) - pageSize + 1; i < (page * pageSize) + 1; i++) {
            Optional<Coin> coin;
            coin = coinsRepository.findById(i);

            if (coin.isPresent()) {
                CoinInfoDTO coinInfoDTO = new CoinInfoDTO();
                coin.get().convertToDTO(coinInfoDTO);
                arrayResponse.add(coinInfoDTO);
            }
            else {
                break;
            }
        }
        response.put("coins", arrayResponse);

        return response;
    }


    /*public Map<Object, Object> getTopList(Filter filter, int page, int pageSize) {
        List<ApiCoin> postFilter = new ArrayList<>();

        for (int i = 0; i < 66; i++) {
            TopListApiResponse topListApiResponse = webClient.get()
                    .uri(String.format("top/totalvolfull?limit=%d&tsym=USD&page=%d", 50, i))
                    .header(apiKeyHeader)
                    .retrieve().bodyToMono(TopListApiResponse.class).block();
            if (topListApiResponse == null || !topListApiResponse.getMessage().equals("Success")) {
                throw new CoinErrorResponse("Filters error");
            }
            List<ApiCoin> apiCoins = topListApiResponse.getData();
            for (ApiCoin apiCoin : apiCoins) {
                if (apiCoin.getCoinPriceData() == null) {
                    continue;
                }
                if (filter.isOkPrice(apiCoin)) {
                    postFilter.add(apiCoin);
                }
            }

        }
        Map<Object, Object> response = new HashMap<>();
        List<CoinInfoDTO> arrayResponse = new ArrayList<>();

        for (int i = (page - 1) * pageSize; i < postFilter.size(); i++) {
            ApiCoin currentFilteredApiCoin = postFilter.get(i);
            CoinInfoDTO coinInfoDTO = new CoinInfoDTO();
            CoinPriceData coinPriceData = currentFilteredApiCoin.getCoinPriceData();
            if (coinPriceData != null) {
                coinPriceData.getPriceInfoUSD().convertToDTO(coinInfoDTO);
            }
            CoinInfo coinInfo = currentFilteredApiCoin.getCoinInfo();

            coinInfo.convertToDTO(coinInfoDTO);

            arrayResponse.add(coinInfoDTO);

            if (arrayResponse.size() == pageSize) {
                break;
            }
        }
        response.put("pageCount", (postFilter.size() / pageSize) + 1);
        response.put("coins", arrayResponse);

        return response;
    }*/

    public DetailedCoinInfoDTO getCoinByTicker(String email, String ticker) throws CoinErrorResponse {
        DetailedCoinInfoDTO response = new DetailedCoinInfoDTO();
        Coin coin = coinsRepository.findByTicker(ticker);
        coin.convertToDTO(response);

        Optional<User> user = usersRepository.findByEmail(email);
        Optional<Note> note = notesRepository.findByUserAndTicker(user.get(), ticker);

        if (note.isPresent()) {
            if (note.get().getNote() == null) {
                response.setNote("");
            }
            else {
                response.setNote(note.get().getNote());
            }

            response.setFavorite(note.get().isFavorite());
        }
        return response;
    }

    public List<CoinPriceHistoryDTO> getPriceHistoryByTicker(String ticker) throws CoinErrorResponse {

        HistoryApiResponse historyApiResponse = webClient.get()
                .uri(String.format("/v2/histoday?fsym=%s&tsym=USD&limit=30", ticker))
                .header(apiKeyHeader)
                .retrieve()
                .bodyToMono(HistoryApiResponse.class)
                .block();
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
            float deltaPrice = curr.subtract(prev)
                    .setScale(Math.max(prev.scale(), curr.scale()), RoundingMode.HALF_UP)
                    .stripTrailingZeros()
                    .floatValue();
            historyList.get(i).convertToDto(coinPriceHistoryDTO, deltaPrice);
            response.add(coinPriceHistoryDTO);
        }
        return response;
    }
}
