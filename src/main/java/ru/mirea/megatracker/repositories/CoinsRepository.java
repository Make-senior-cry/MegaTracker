package ru.mirea.megatracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mirea.megatracker.models.Coin;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoinsRepository extends JpaRepository<Coin, Integer> {
    Optional<Coin> findByTicker(String ticker);

    @Query("SELECT c FROM Coin c WHERE c.currentPrice >= :minPrice AND c.currentPrice <= :maxPrice AND c.deltaPrice >" +
            " :minDeltaPrice ORDER BY c.id")
    List<Coin> findAllWithFilters(@Param("minPrice") float minPrice,
                                  @Param("maxPrice") float maxPrice, @Param("minDeltaPrice") float minDeltaPrice);
}
//select c from Coin c where c.currentPrice >= :minPrice and c.currentPrice <= :maxPrice and (((c.deltaPrice > 0.001) and :isRising) or (not :isRising)) order by c.id