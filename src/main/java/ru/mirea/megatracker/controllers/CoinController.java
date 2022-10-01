package ru.mirea.megatracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mirea.megatracker.dto.coin.CoinInfoDTO;
import ru.mirea.megatracker.dto.coin.CoinPriceHistoryDTO;
import ru.mirea.megatracker.dto.coin.DetailedCoinInfoDTO;
import ru.mirea.megatracker.security.jwt.JwtUtil;
import ru.mirea.megatracker.services.CoinService;
import ru.mirea.megatracker.services.NoteService;
import ru.mirea.megatracker.util.CoinErrorResponse;
import ru.mirea.megatracker.util.UserErrorResponse;
import ru.mirea.megatracker.util.UserNotAuthenticatedException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/coins")
public class CoinController {

    private final CoinService coinService;
    private final NoteService noteService;
    private final JwtUtil jwtUtil;


    @Autowired
    public CoinController(CoinService coinService, NoteService noteService, JwtUtil jwtUtil) {
        this.coinService = coinService;
        this.noteService = noteService;
        this.jwtUtil = jwtUtil;
    }



    @GetMapping()
    public ResponseEntity<?> getCoins(@RequestParam(value = "f", required = false) String filters, @RequestParam int page,
                                      @RequestParam int pageSize){
        return new ResponseEntity<List<CoinInfoDTO>>(coinService.getTopList(null, page, pageSize), HttpStatus.OK);
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
    public ResponseEntity<?> addNote(@PathVariable String ticker, HttpServletRequest request,
                                     @RequestBody Map<String, String> requestBody) {
        String token = request.getHeader("Authorization").substring(7);
        noteService.addNoteForCoin(jwtUtil.getUsernameFromJwtToken(token), ticker, requestBody.get("note"));
        return ResponseEntity.ok("Note added successfully!");
    }

    @PostMapping("/{ticker}/set-favorite")
    public ResponseEntity<?> setFavorite(@PathVariable String ticker, HttpServletRequest request,
                                         @RequestBody Map<String, Boolean> requestBody) {
        String token = request.getHeader("Authorization").substring(7);
        noteService.setFavoriteForCoin(jwtUtil.getUsernameFromJwtToken(token), ticker, requestBody.get("isFavorite"));
        return ResponseEntity.ok("Coin condition successfully updated!");
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
