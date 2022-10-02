package ru.mirea.megatracker.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.api.coin.Coin;
import ru.mirea.megatracker.api.coin.PriceInfoUSD;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Filter {

    private float minPrice = -1;

    private float maxPrice = Float.MAX_VALUE;

    private long minCap = 0;

    private long maxCap = Long.MAX_VALUE;

    private boolean isIncreased;



    public boolean isOk(Coin coin){
        PriceInfoUSD priceInfo = coin.getCoinPriceData().getPriceInfoUSD();
        return minPrice <= priceInfo.getCurrentPrice().floatValue() &&
                maxPrice >= priceInfo.getCurrentPrice().floatValue() &&
                minCap <= priceInfo.getMarketCap() &&
                maxCap >= priceInfo.getMarketCap() &&
                0 < priceInfo.getDeltaPrice().floatValue();
    }


}
