package ru.mirea.megatracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mirea.megatracker.dto.CoinInfoDTO;
import ru.mirea.megatracker.dto.CoinPriceHistoryDTO;
import ru.mirea.megatracker.dto.DetailedCoinInfoDTO;
import ru.mirea.megatracker.services.CoinService;
import ru.mirea.megatracker.util.CoinErrorResponse;
import ru.mirea.megatracker.util.UserErrorResponse;
import ru.mirea.megatracker.util.UserNotAuthenticatedException;

import java.util.List;

//TODO

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/coins")
public class CoinConroller {

    private final CoinService coinService;


    @Autowired
    public CoinConroller(CoinService coinService) {
        this.coinService = coinService;
    }



    @GetMapping()
    public ResponseEntity<?> getCoins(@RequestParam(value = "f", required = false) String filters){
        return new ResponseEntity<List<CoinInfoDTO>>(coinService.getTopList(null, 10), HttpStatus.OK);
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<?> getCoinByTicker(@PathVariable String ticker){
        return new ResponseEntity<DetailedCoinInfoDTO>(coinService.getCoinByTicker(ticker), HttpStatus.OK);
    }


    @GetMapping("/{ticker}/history")
    public ResponseEntity<?> getHistoryByTicker(@PathVariable String ticker){
        return new ResponseEntity<List<CoinPriceHistoryDTO>>(coinService.getPriceHistoryByTicker(ticker), HttpStatus.OK);
    }

    @GetMapping("/favorite")
    public ResponseEntity<?> getFavorite(){
        return ResponseEntity.ok().build();
    }


    @ExceptionHandler
    private ResponseEntity<CoinErrorResponse> handleException(CoinErrorResponse exception){
        CoinErrorResponse response = new CoinErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotAuthenticatedException exception) {
        UserErrorResponse response = new UserErrorResponse(exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
