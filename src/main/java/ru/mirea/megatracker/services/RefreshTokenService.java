package ru.mirea.megatracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.ExpiredJwtException;
import ru.mirea.megatracker.exceptions.UserNotFoundException;
import ru.mirea.megatracker.models.RefreshToken;
import ru.mirea.megatracker.models.User;
import ru.mirea.megatracker.repositories.RefreshTokensRepository;
import ru.mirea.megatracker.repositories.UsersRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class RefreshTokenService {
    private final RefreshTokensRepository refreshTokensRepository;
    private final UsersRepository usersRepository;
    @Value("${jwtRefresh.token.expired}")
    private Long refreshTokenDuration;

    @Autowired
    public RefreshTokenService(RefreshTokensRepository refreshTokensRepository, UsersRepository usersRepository) {
        this.refreshTokensRepository = refreshTokensRepository;
        this.usersRepository = usersRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokensRepository.findByToken(token);
    }

    @Transactional
    public RefreshToken createRefreshToken(int userId) {
        RefreshToken refreshToken = new RefreshToken();

        Optional<User> maybeUser = usersRepository.findById(userId);
        if (maybeUser.isEmpty()) throw new UserNotFoundException();

        refreshToken.setUser(maybeUser.get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDuration));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokensRepository.save(refreshToken);
    }

    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        boolean tokenIsExpired = token.getExpiryDate().compareTo(Instant.now()) < 0;
        if (tokenIsExpired) {
            refreshTokensRepository.delete(token);
            throw new ExpiredJwtException(null, null, "Authentication failed");
        }

        return token;
    }
}
