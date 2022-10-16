package ru.mirea.megatracker.interfaces;

import java.util.Optional;

import ru.mirea.megatracker.models.RefreshToken;

public interface RefreshTokenService {
    public Optional<RefreshToken> findByToken(String token);
    public RefreshToken createRefreshToken(int userId);
    public RefreshToken verifyExpiration(RefreshToken token);
}
