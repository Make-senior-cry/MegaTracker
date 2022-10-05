package ru.mirea.megatracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.mirea.megatracker.models.Coin;

import java.util.List;

public interface CoinsRepository extends JpaRepository<Coin, Integer> {
    Coin findByTicker(String ticker);
    boolean existsByTicker(String ticker);

    @Query("select c from Coin c where c.currentPrice >= :minPrice and c.currentPrice <= :maxPrice order by c.id")
    List<Coin> findAllWithFilters(@Param("minPrice") float minPrice,
                                  @Param("maxPrice") float maxPrice);

    @Query("select c from Coin c where c.currentPrice >= :minPrice and c.currentPrice <= :maxPrice and c.deltaPrice > 0" +
            "order by c.id")
    List<Coin> findAllRisingWithFilters(@Param("minPrice") float minPrice,
                                        @Param("maxPrice") float maxPrice);

    List<Coin> findByNameStartingWithIgnoreCaseOrTickerStartingWithIgnoreCase(String beginningOfName, String beginningOfTicker);
}
