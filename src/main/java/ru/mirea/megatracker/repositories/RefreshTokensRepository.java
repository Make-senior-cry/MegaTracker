package ru.mirea.megatracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import ru.mirea.megatracker.models.RefreshToken;
import ru.mirea.megatracker.models.User;

import java.util.Optional;

@Repository
public interface RefreshTokensRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(User user);
}
