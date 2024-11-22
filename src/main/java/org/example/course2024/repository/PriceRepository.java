package org.example.course2024.repository;

import org.example.course2024.entity.Master;
import org.example.course2024.entity.Price;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long> {

    @Query("SELECT p FROM Price p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    Page<Price> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    @Query("from Price")
    public List<Price> findAllByPage(PageRequest pageRequest);

    @Query("SELECT p FROM Price p WHERE p.master.id = :idMaster")
    List<Price> findByMaster(Long idMaster);
}
