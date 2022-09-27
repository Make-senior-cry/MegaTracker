package ru.mirea.megatracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mirea.megatracker.api.*;
import ru.mirea.megatracker.dto.CoinInfoDTO;
import ru.mirea.megatracker.dto.CoinPriceHistoryDTO;
import ru.mirea.megatracker.dto.DetailedCoinInfoDTO;
import ru.mirea.megatracker.util.CoinErrorResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoinService {
    private final WebClient webClient;

    @Value("${api.key}")
    private String apiKey;

    private final String apiKeyHeader;

    @Autowired
    public CoinService(WebClient webClient) {
        this.webClient = webClient;
        this.apiKeyHeader = "Apikey {" + apiKey + "}";
    }

    public List<CoinInfoDTO> getTopList(List<?> filters, int pageSize) throws CoinErrorResponse {
        TopListApiResponse topListApiResponse = webClient.get()
                .uri(String.format("top/totalvolfull?limit=%d&tsym=USD", pageSize))
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
                coinInfo = coin.getCoinInfo();

                coinPriceData.getPriceInfoUSD().convertToDTO(coinInfoDTO);
                coinInfo.convertToDTO(coinInfoDTO);

                response.add(coinInfoDTO);
            }
            return response;
        } else {
            throw new CoinErrorResponse("External API error");
        }
    }

    public DetailedCoinInfoDTO getCoinByTicker(String ticker) throws CoinErrorResponse {
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


        PriceApiResponse priceApiResponse = webClient.get()
                .uri(String.format("/price?fsym=%s&tsyms=USD", ticker))
                .header(apiKeyHeader)
                .retrieve()
                .bodyToMono(PriceApiResponse.class)
                .block();
        if (priceApiResponse == null) {
            throw new CoinErrorResponse("External API error 2");
        }
        response.setPrice(priceApiResponse.getPrice());


        return response;
    }

    public CoinPriceHistoryDTO getPriceHistoryByTicker(String ticker) throws CoinErrorResponse{

        HistoryApiResponse historyApiResponse = webClient.get()
                .uri(String.format("/v2/histoday?fsym=%s&tsym=USD&limit=24", ticker))
                .header(apiKeyHeader)
                .retrieve()
                .bodyToMono(HistoryApiResponse.class)
                .block();

        return null;
    }


}
