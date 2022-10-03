package ru.mirea.megatracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.mirea.megatracker.models.Coin;

public interface CoinsRepository extends JpaRepository<Coin, Integer> {
    Coin findByTicker(String ticker);
    boolean existsByTicker(String ticker);
}
