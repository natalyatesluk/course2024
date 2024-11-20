package org.example.course2024.controller;

import org.example.course2024.dto.PriceCreationDto;
import org.example.course2024.dto.PriceDto;
import org.example.course2024.dto.PriceUpdatingDto;
import org.example.course2024.service.PriceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/price")
public class PriceController {
    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping()
    public ResponseEntity<List<PriceDto>> getAllPrices() {
        return new ResponseEntity<>(priceService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceDto> getPriceById(@PathVariable Long id) {
        return new ResponseEntity<>(priceService.getById(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<PriceDto> createPrice(@RequestBody PriceCreationDto priceDto) {
        return new ResponseEntity<>(priceService.create(priceDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PriceDto> updatePrice(@PathVariable Long id, @RequestBody PriceUpdatingDto priceDto) {
        return new ResponseEntity<>(priceService.update(id, priceDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PriceDto> deletePrice(@PathVariable Long id) {
        priceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/range")
    public ResponseEntity<List<PriceDto>> getPricesInRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<PriceDto> prices = priceService.getPricesInRange(minPrice, maxPrice);
        return new ResponseEntity<>(prices, HttpStatus.OK);
    }
}