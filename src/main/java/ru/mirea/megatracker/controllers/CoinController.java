package ru.mirea.megatracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mirea.megatracker.interfaces.CoinService;
import ru.mirea.megatracker.interfaces.NoteService;
import ru.mirea.megatracker.security.jwt.JwtUtil;
import ru.mirea.megatracker.util.CoinErrorResponse;
import ru.mirea.megatracker.util.UserErrorResponse;
import ru.mirea.megatracker.util.UserNotAuthenticatedException;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> getCoins(@RequestParam(value = "minPrice") float minPrice,
                                      @RequestParam(value = "maxPrice") float maxPrice,
                                      @RequestParam(value = "isRising") boolean isRising,
                                      @RequestParam(value = "page") int page,
                                      @RequestParam(value = "pageSize") int pageSize,
                                      @RequestParam(value = "search") String search) {
        if (maxPrice == 0) maxPrice = Float.MAX_VALUE;

        return new ResponseEntity<>(coinService.getTopList(page, pageSize, minPrice, maxPrice, isRising, search), HttpStatus.OK);
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<?> getCoinByTicker(@PathVariable String ticker, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return new ResponseEntity<>(coinService.getCoinByTicker(jwtUtil.getUsernameFromJwtToken(token),
                ticker), HttpStatus.OK);
    }

    @GetMapping("/{ticker}/history")
    public ResponseEntity<?> getHistoryByTicker(@PathVariable String ticker) {
        return new ResponseEntity<>(coinService.getPriceHistoryByTicker(ticker), HttpStatus.OK);
    }

    @PostMapping("/{ticker}/set-note")
    public ResponseEntity<?> setNote(@PathVariable String ticker, HttpServletRequest request,
                                     @RequestBody Map<String, String> requestBody) {
        String token = request.getHeader("Authorization").substring(7);
        noteService.setNoteForCoin(jwtUtil.getUsernameFromJwtToken(token), ticker, requestBody.get("note"));
        return ResponseEntity.ok("Note added successfully!");
    }

    @PostMapping("/{ticker}/set-favorite")
    public ResponseEntity<?> setFavorite(@PathVariable String ticker, HttpServletRequest request,
                                         @RequestBody Map<String, Boolean> requestBody) {
        String token = request.getHeader("Authorization").substring(7);
        noteService.setFavoriteForCoin(jwtUtil.getUsernameFromJwtToken(token), ticker, requestBody.get("isFavorite"));
        return ResponseEntity.ok("Coin condition successfully updated!");
    }

    @GetMapping("/favorite")
    public ResponseEntity<?> getFavoriteCoins(HttpServletRequest headers,
                                              @RequestParam int page,
                                              @RequestParam int pageSize) {
        String token = headers.getHeader("Authorization").substring(7);
        return new ResponseEntity<>(coinService.getFavoriteCoins(page, pageSize,
                jwtUtil.getUsernameFromJwtToken(token)), HttpStatus.OK);
    }


    @ExceptionHandler
    private ResponseEntity<CoinErrorResponse> handleException(CoinErrorResponse exception) {
        CoinErrorResponse response = new CoinErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotAuthenticatedException exception) {
        UserErrorResponse response = new UserErrorResponse(exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
