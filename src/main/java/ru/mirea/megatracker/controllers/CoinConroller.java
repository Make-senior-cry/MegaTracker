package ru.mirea.megatracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mirea.megatracker.dto.Coin;
import ru.mirea.megatracker.services.CoinService;
import ru.mirea.megatracker.util.CoinErrorResponse;
import ru.mirea.megatracker.util.TickerNotFoundException;
import ru.mirea.megatracker.util.UserErrorResponse;
import ru.mirea.megatracker.util.UserNotAuthenticatedException;

import java.util.List;

//TODO

@RestController
@RequestMapping("/coins")
public class CoinConroller {

    private final CoinService coinService;


    @Autowired
    public CoinConroller(CoinService coinService) {
        this.coinService = coinService;
    }



    @GetMapping("")
    public ResponseEntity<?> getCoins(@RequestParam(value = "f", required = false) String filters){
        ///List<Coin> ls = coinService.getTopList(null, 0, 10);
        return new ResponseEntity<List<Coin>>(coinService.getTopList(null, 10), HttpStatus.OK);
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<?> getCoinByTicker(@PathVariable String ticker){
        //return new ResponseEntity<Double>(coinService.getPrice(), HttpStatus.OK);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{ticker}/history")
    public ResponseEntity<?> getHistoryByTicker(@PathVariable String ticker){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorite")
    public ResponseEntity<?> getFavorite(){
        return ResponseEntity.ok().build();
    }


    @ExceptionHandler
    private ResponseEntity<CoinErrorResponse> handleException(TickerNotFoundException exception){
        CoinErrorResponse response = new CoinErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotAuthenticatedException exception) {
        UserErrorResponse response = new UserErrorResponse(exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
