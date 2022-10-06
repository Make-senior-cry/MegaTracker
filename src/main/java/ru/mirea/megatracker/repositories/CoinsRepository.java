package ru.mirea.megatracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mirea.megatracker.models.Coin;

import java.util.List;

@Repository
public interface CoinsRepository extends JpaRepository<Coin, Integer> {
    Coin findByTicker(String ticker);

    boolean existsByTicker(String ticker);

    @Query("select c from Coin c where c.currentPrice >= :minPrice and c.currentPrice <= :maxPrice order by c.id")
    List<Coin> findAllWithFilters(@Param("minPrice") float minPrice,
                                  @Param("maxPrice") float maxPrice);

    @Query("select c from Coin c where c.currentPrice >= :minPrice and c.currentPrice <= :maxPrice and c.deltaPrice > 0.001" +
            "order by c.id")
    List<Coin> findAllRisingWithFilters(@Param("minPrice") float minPrice,
                                        @Param("maxPrice") float maxPrice);

    @Query("select c from Coin c where lower(c.name) like %:name% or lower(c.ticker) like %:ticker%")
    List<Coin> findAllBySearch(String name, String ticker);
}
