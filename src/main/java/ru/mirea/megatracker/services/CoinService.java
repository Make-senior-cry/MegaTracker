package ru.mirea.megatracker.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mirea.megatracker.api.ApiResponse;
import ru.mirea.megatracker.api.Coin;
import ru.mirea.megatracker.api.CoinInfo;
import ru.mirea.megatracker.api.CoinPriceData;
import ru.mirea.megatracker.dto.CoinInfoDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoinService {
    private final WebClient webClient;

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    public CoinService(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<CoinInfoDTO> getTopList(List<?> filters, int pageSize){
        ApiResponse apiResponse = webClient.get().
                uri(String.format("/data/top/totalvolfull?limit=%d&tsym=USD", pageSize)).
                header("Apikey {" + apiKey + "}").
                retrieve().bodyToMono(ApiResponse.class).block();

        List<CoinInfoDTO> response = new ArrayList<>(pageSize);

        if (apiResponse.getMessage().equals("Success")) {
            List<Coin> coins = apiResponse.getData();
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
        }

        return response;
    }





}
