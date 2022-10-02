package ru.mirea.megatracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mirea.megatracker.api.coin.Coin;
import ru.mirea.megatracker.api.coin.CoinHistoryPrice;
import ru.mirea.megatracker.api.coin.CoinInfo;
import ru.mirea.megatracker.api.coin.CoinPriceData;
import ru.mirea.megatracker.api.response.ExchangePairApiResponse;
import ru.mirea.megatracker.api.response.HistoryApiResponse;
import ru.mirea.megatracker.api.response.TopListApiResponse;
import ru.mirea.megatracker.dto.coin.CoinInfoDTO;
import ru.mirea.megatracker.dto.coin.CoinPriceHistoryDTO;
import ru.mirea.megatracker.dto.coin.DetailedCoinInfoDTO;
import ru.mirea.megatracker.models.Note;
import ru.mirea.megatracker.models.User;
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

    @Autowired
    public CoinService(WebClient webClient, UsersRepository usersRepository, NotesRepository notesRepository) {
        this.webClient = webClient;
        this.usersRepository = usersRepository;
        this.notesRepository = notesRepository;
        this.apiKeyHeader = "Apikey {" + apiKey + "}";
    }

    public List<CoinInfoDTO> getTopList(int page, int pageSize) throws CoinErrorResponse {
        TopListApiResponse topListApiResponse = webClient.get()
                .uri(String.format("top/totalvolfull?limit=%d&tsym=USD&page=%d", pageSize, page - 1))
                .header(apiKeyHeader)
                .retrieve().bodyToMono(TopListApiResponse.class).block();

        List<CoinInfoDTO> response = new ArrayList<>(pageSize);

        if (topListApiResponse != null && topListApiResponse.getMessage().equals("Success")) {
            List<Coin> coins = topListApiResponse.getData();
            CoinPriceData coinPriceData;
            CoinInfo coinInfo;

            for (Coin coin : coins) {
                CoinInfoDTO coinInfoDTO = new CoinInfoDTO();
                coinPriceData = coin.getCoinPriceData();
                if (coinPriceData != null) {
                    coinPriceData.getPriceInfoUSD().convertToDTO(coinInfoDTO);
                }
                coinInfo = coin.getCoinInfo();


                coinInfo.convertToDTO(coinInfoDTO);

                response.add(coinInfoDTO);
            }
            return response;
        } else {
            throw new CoinErrorResponse("External API error");
        }
    }


    public Map<Object, Object> getTopList(Filter filter, int page, int pageSize) {
        List<Coin> postFilter = new ArrayList<>();

        for (int i = 0; i < 66; i++) {
            TopListApiResponse topListApiResponse = webClient.get()
                    .uri(String.format("top/totalvolfull?limit=%d&tsym=USD&page=%d", 50, i))
                    .header(apiKeyHeader)
                    .retrieve().bodyToMono(TopListApiResponse.class).block();
            if (topListApiResponse == null || !topListApiResponse.getMessage().equals("Success")) {
                throw new CoinErrorResponse("Filters error");
            }
            List<Coin> coins = topListApiResponse.getData();
            for (Coin coin : coins) {
                if (coin.getCoinPriceData() == null) {
                    continue;
                }
                if (filter.isOkPrice(coin)) {
                    postFilter.add(coin);
                }
            }

        }
        Map<Object, Object> response = new HashMap<>();
        List<CoinInfoDTO> arrayResponse = new ArrayList<>();

        for (int i = (page - 1) * pageSize; i < postFilter.size(); i++) {
            Coin currentFilteredCoin = postFilter.get(i);
            CoinInfoDTO coinInfoDTO = new CoinInfoDTO();
            CoinPriceData coinPriceData = currentFilteredCoin.getCoinPriceData();
            if (coinPriceData != null) {
                coinPriceData.getPriceInfoUSD().convertToDTO(coinInfoDTO);
            }
            CoinInfo coinInfo = currentFilteredCoin.getCoinInfo();

            coinInfo.convertToDTO(coinInfoDTO);

            arrayResponse.add(coinInfoDTO);

            if (arrayResponse.size() == pageSize) {
                break;
            }
        }
        response.put("pageCount", (postFilter.size() / pageSize) + 1);
        response.put("coins", arrayResponse);

        return response;
    }

    public DetailedCoinInfoDTO getCoinByTicker(String email, String ticker) throws CoinErrorResponse {
        ExchangePairApiResponse exchangePairApiResponse = webClient.get()
                .uri(String.format("/top/exchanges/full?fsym=%s&tsym=USD&limit=1", ticker))
                .header(apiKeyHeader)
                .retrieve()
                .bodyToMono(ExchangePairApiResponse.class)
                .block();
        DetailedCoinInfoDTO response = new DetailedCoinInfoDTO();

        if (exchangePairApiResponse == null || !exchangePairApiResponse.getMessage().equals("Success")) {
            throw new CoinErrorResponse("External API error 1");
        }
        exchangePairApiResponse.getData().getCoinInfo().convertToDTO(response);
        exchangePairApiResponse.getData().getPriceInfoUSD().convertToDTO(response);

        Optional<User> user = usersRepository.findByEmail(email);
        Optional<Note> note = notesRepository.findByUserAndTicker(user.get(), ticker);

        if (note.isPresent()) {
            response.setNote(note.get().getNote());
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
