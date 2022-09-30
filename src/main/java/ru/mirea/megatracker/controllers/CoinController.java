package ru.mirea.megatracker.controllers;

import liquibase.pro.packaged.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mirea.megatracker.dto.coin.CoinInfoDTO;
import ru.mirea.megatracker.dto.coin.CoinPriceHistoryDTO;
import ru.mirea.megatracker.dto.coin.DetailedCoinInfoDTO;
import ru.mirea.megatracker.services.CoinService;
import ru.mirea.megatracker.services.NoteService;
import ru.mirea.megatracker.util.CoinErrorResponse;
import ru.mirea.megatracker.util.UserErrorResponse;
import ru.mirea.megatracker.util.UserNotAuthenticatedException;

import java.util.List;

//TODO

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/coins")
public class CoinController {

    private final CoinService coinService;
    private final NoteService noteService;


    @Autowired
    public CoinController(CoinService coinService, NoteService noteService) {
        this.coinService = coinService;
        this.noteService = noteService;
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

    @PostMapping("/{ticker}/note")
    public ResponseEntity<?> addNote(@PathVariable String ticker, @RequestBody String email, @RequestBody String note) {
        noteService.addNoteForCoin(email, ticker, note);
        return ResponseEntity.ok("Note added successfully!");
    }

    @PostMapping("/{ticker}/set-favorite")
    public ResponseEntity<?> setFavorite(@PathVariable String ticker, @RequestBody boolean isFavorite) {

        return null;
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
