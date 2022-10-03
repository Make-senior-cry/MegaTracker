package ru.mirea.megatracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mirea.megatracker.models.Coin;

public interface CoinsRepository extends JpaRepository<Coin, String> {
}
