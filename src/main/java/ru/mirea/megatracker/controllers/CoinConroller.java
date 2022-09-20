package ru.mirea.megatracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mirea.megatracker.repositories.CoinRepository;
import ru.mirea.megatracker.util.CoinErrorResponse;
import ru.mirea.megatracker.util.TickerNotFoundException;
import ru.mirea.megatracker.util.UserErrorResponse;
import ru.mirea.megatracker.util.UserNotAuthenticatedException;

//TODO

@RestController
@RequestMapping("/coins")
public class CoinConroller {

    @Autowired
    CoinRepository coinRepository;


    @GetMapping("")
    public ResponseEntity<?> getCoins(@RequestParam(value = "f", required = false) String filters,
                                      BindingResult bindingResult){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<?> getCoinByTicker(@PathVariable String ticker,
                                             BindingResult bindingResult){
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{ticker}/history")
    public ResponseEntity<?> getHistoryByTicker(@PathVariable String ticker,
                                                BindingResult bindingResult){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorite")
    public ResponseEntity<?> getFavorite(BindingResult bindingResult){
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
