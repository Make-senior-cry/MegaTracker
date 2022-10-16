package ru.mirea.megatracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.mirea.megatracker.interfaces.IRefreshTokenService;
import ru.mirea.megatracker.models.RefreshToken;
import ru.mirea.megatracker.repositories.RefreshTokensRepository;
import ru.mirea.megatracker.repositories.UsersRepository;
import ru.mirea.megatracker.util.TokenRefreshException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class RefreshTokenService implements IRefreshTokenService {
    @Value("${jwtRefresh.token.expired}")
    private Long refreshTokenDuration;

    private final RefreshTokensRepository refreshTokensRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public RefreshTokenService(RefreshTokensRepository refreshTokensRepository, UsersRepository usersRepository) {
        this.refreshTokensRepository = refreshTokensRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokensRepository.findByToken(token);
    }

    @Transactional
    @Override
    public RefreshToken createRefreshToken(int userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(usersRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDuration));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokensRepository.save(refreshToken);
        return refreshToken;
    }

    @Transactional
    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokensRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new sign in request");
        }

        return token;
    }
}
