package ru.mirea.megatracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mirea.megatracker.dto.coin.CoinPriceHistoryDTO;
import ru.mirea.megatracker.dto.coin.DetailedCoinInfoDTO;
import ru.mirea.megatracker.security.jwt.JwtUtil;
import ru.mirea.megatracker.services.CoinService;
import ru.mirea.megatracker.services.NoteService;
import ru.mirea.megatracker.util.CoinErrorResponse;
import ru.mirea.megatracker.util.Filter;
import ru.mirea.megatracker.util.UserErrorResponse;
import ru.mirea.megatracker.util.UserNotAuthenticatedException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public ResponseEntity<?> getCoins(@RequestParam(value = "minPrice", required = false) Optional<Float> minPrice,
                                      @RequestParam(value = "maxPrice", required = false) Optional<Float> maxPrice,
                                      @RequestParam(value = "isIncreased", required = false) Optional<Boolean> isIncreased,
                                      @RequestParam int page,
                                      @RequestParam int pageSize) {
        Filter filter = new Filter();
        minPrice.ifPresent(filter::setMinPrice);
        maxPrice.ifPresent(filter::setMaxPrice);
        isIncreased.ifPresent(filter::setIncreased);

        return new ResponseEntity<>(coinService.getTopList(filter , page, pageSize), HttpStatus.OK);
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<?> getCoinByTicker(@PathVariable String ticker, HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        return new ResponseEntity<DetailedCoinInfoDTO>(coinService.getCoinByTicker(jwtUtil.getUsernameFromJwtToken(token),
                ticker), HttpStatus.OK);
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
