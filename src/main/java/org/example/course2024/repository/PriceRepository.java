package org.example.course2024.repository;

import org.example.course2024.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long> {

    @Query("SELECT p FROM Price p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Price> findByPriceRange(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice);

}
