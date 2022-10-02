package ru.mirea.megatracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.mirea.megatracker.dto.user.SignInUserDTO;
import ru.mirea.megatracker.dto.user.SignUpUserDTO;
import ru.mirea.megatracker.models.RefreshToken;
import ru.mirea.megatracker.models.User;
import ru.mirea.megatracker.payload.JwtResponse;
import ru.mirea.megatracker.payload.TokenRefreshRequest;
import ru.mirea.megatracker.payload.TokenRefreshResponse;
import ru.mirea.megatracker.security.UserDetailsImpl;
import ru.mirea.megatracker.security.jwt.JwtUtil;
import ru.mirea.megatracker.services.AuthService;
import ru.mirea.megatracker.services.RefreshTokenService;
import ru.mirea.megatracker.util.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, AuthService authService,
                          RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }


    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpUserDTO signUpUserDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getDefaultMessage()).append("\n");
            }

            throw new UserNotCreatedException(errorMessage.toString());
        }

        if (authService.checkForEmailExistence(signUpUserDTO.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new UserErrorResponse("Email is already in use!"));
        }

        confirmPassword(signUpUserDTO.getPassword(), signUpUserDTO.getRepeatedPassword());

        User user = signUpUserDTO.toUser();
        authService.register(user);

        return ResponseEntity.ok(new UserErrorResponse("User registered successfully!"));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInUserDTO signInUserDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getDefaultMessage()).append("\n");
            }

            throw new UserNotAuthenticatedException(errorMessage.toString());
        }

        authService.checkRefreshToken(signInUserDTO.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInUserDTO.getEmail(), signInUserDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String accessToken = jwtUtil.generateJwtToken(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken.getToken()));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody @Valid TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtUtil.generateTokenFromUsername(user.getEmail());
                    return ResponseEntity.ok(new TokenRefreshResponse(accessToken, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logOut(@RequestBody Map<String, String> request) {
        authService.logOut(request.get("refreshToken"));
        return ResponseEntity.ok(new UserErrorResponse("User logged out successfully!"));
    }


    private void confirmPassword(String password, String repeatedPassword) {
        if (!password.equals(repeatedPassword)) {
            throw new UnconfirmedPasswordException("Password does not match!");
        }
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotCreatedException exception) {
        UserErrorResponse response = new UserErrorResponse(exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UnconfirmedPasswordException exception) {
        UserErrorResponse response = new UserErrorResponse(exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotAuthenticatedException exception) {
        UserErrorResponse response = new UserErrorResponse(exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(TokenRefreshException exception) {
        UserErrorResponse response = new UserErrorResponse(exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
