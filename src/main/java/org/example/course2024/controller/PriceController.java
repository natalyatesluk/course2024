package org.example.course2024.controller;

import org.example.course2024.dto.*;
import org.example.course2024.service.PriceService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Cacheable(value = "price", key = "'page_' + #page + '_size_' + #size")
    @GetMapping()
    public ResponseEntity<PagedDataDto<PriceDto>> getAllPrices(
            @RequestParam (required = false, defaultValue = "0") int page,
            @RequestParam (required = false, defaultValue = "6") int size
    ) {
        return new ResponseEntity<>(priceService.getAll(PageRequest.of(page,size)), HttpStatus.OK);
    }

    @Cacheable(value = "price", key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<PriceDto> getPriceById(@PathVariable Long id) {
        return new ResponseEntity<>(priceService.getById(id), HttpStatus.OK);
    }

    @CachePut(value = "price")
    @PostMapping()
    public ResponseEntity<PriceDto> createPrice(@RequestBody PriceCreationDto priceDto) {
        return new ResponseEntity<>(priceService.create(priceDto), HttpStatus.CREATED);
    }

    @CachePut(value = "price", key = "#id")
    @PutMapping("/{id}")
    public ResponseEntity<PriceDto> updatePrice(@PathVariable Long id, @RequestBody PriceUpdatingDto priceDto) {
        return new ResponseEntity<>(priceService.update(id, priceDto), HttpStatus.OK);
    }

    @CacheEvict(value = "price", key = "#id")
    @DeleteMapping("/{id}")
    public ResponseEntity<PriceDto> deletePrice(@PathVariable Long id) {
        priceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Cacheable(value = "price", key = "'search_' + #minPrice + '_to_' + #maxPrice + '_page_' + #page + '_size_' + #size + '_asc_' + #asc")
    @GetMapping("/range")
    public ResponseEntity<PagedDataDto<PriceDto>> getPricesInRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "3") int size,
            boolean asc) {
        PagedDataDto<PriceDto> prices = priceService.getPricesInRange(minPrice, maxPrice, page, size, asc);
        return new ResponseEntity<>(prices, HttpStatus.OK);
    }
    @Cacheable(value = "price", key = "'search_' + #keyword + '_page_' + #page + '_size_' + #size")
    @GetMapping("/search")
    public ResponseEntity<PagedDataDto<PriceDto>> searchPrice(@RequestParam String keyword,
                                                              @RequestParam(required = false, defaultValue = "0") int page,
                                                              @RequestParam(required = false, defaultValue = "3") int size) {
        return new ResponseEntity<>(priceService.search(keyword, PageRequest.of(page, size)), HttpStatus.OK);
    }

    @Cacheable(value = "price", key = "'sort_' + #keyword + '_reverse_' + #reverse + '_page_' + #page + '_size_' + #size")
    @GetMapping("/sort")
    public ResponseEntity<PagedDataDto<PriceDto>> sortPrice(@RequestParam String keyword,
                                                            @RequestParam(defaultValue = "false") boolean reverse,
                                                            @RequestParam(required = false, defaultValue = "0") int page,
                                                            @RequestParam(required = false, defaultValue = "6") int size) {

        Sort sort = Sort.by(reverse ? Sort.Direction.DESC : Sort.Direction.ASC, keyword);
        return new ResponseEntity<>(priceService.getAll(PageRequest.of(page, size, sort)), HttpStatus.OK);
    }
}