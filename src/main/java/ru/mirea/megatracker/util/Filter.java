package ru.mirea.megatracker.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.megatracker.api.coin.ApiCoin;
import ru.mirea.megatracker.api.coin.PriceInfoUSD;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Filter {
    private float minPrice = -1;
    private float maxPrice = Float.MAX_VALUE;

    private boolean isIncreased;

    public boolean isOkPrice(ApiCoin apiCoin){
        PriceInfoUSD priceInfo = apiCoin.getCoinPriceData().getPriceInfoUSD();
        return minPrice <= priceInfo.getCurrentPrice().floatValue() &&
                maxPrice >= priceInfo.getCurrentPrice().floatValue();
    }

    public boolean isOkIncreaced(ApiCoin apiCoin) {
        PriceInfoUSD priceInfo = apiCoin.getCoinPriceData().getPriceInfoUSD();
        return (priceInfo.getDeltaPrice().floatValue() > 0) == isIncreased;
    }


}
