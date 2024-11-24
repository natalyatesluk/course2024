package org.example.course2024.controller;

import jakarta.validation.Valid;
import org.example.course2024.dto.*;
import org.example.course2024.service.PriceService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/price")
@Tag(name = "Price API", description = "Operations for managing Prices")
public class PriceController {
    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @Operation(summary = "Get all Prices", description = "Retrieve a paginated list of Prices")
    @Cacheable(value = "price", key = "'page_' + #page + '_size_' + #size")
    @GetMapping()
    public ResponseEntity<PagedDataDto<PriceDto>> getAllPrices(
            @Parameter(description = "Page number for pagination") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size for pagination") @RequestParam(required = false, defaultValue = "6") int size) {
        return new ResponseEntity<>(priceService.getAll(PageRequest.of(page, size)), HttpStatus.OK);
    }

    @Operation(summary = "Get Price by ID", description = "Retrieve a single Price by its unique ID")
    @Cacheable(value = "price", key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<PriceDto> getPriceById(
            @Parameter(description = "ID of the Price to retrieve") @PathVariable Long id) {
        return new ResponseEntity<>(priceService.getById(id), HttpStatus.OK);
    }

    @Operation(summary = "Create a new Price", description = "Add a new Price to the system")
    @CachePut(value = "price")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<PriceDto> createPrice(@Valid @RequestBody PriceCreationDto priceDto) {
        return new ResponseEntity<>(priceService.create(priceDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update Price by ID", description = "Update details of an existing Price by its ID")
    @CachePut(value = "price", key = "#id")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PriceDto> updatePrice(
            @Parameter(description = "ID of the Price to update") @PathVariable Long id,
            @Valid @RequestBody PriceUpdatingDto priceDto) {
        return new ResponseEntity<>(priceService.update(id, priceDto), HttpStatus.OK);
    }

    @Operation(summary = "Delete Price by ID", description = "Delete an existing Price by its unique ID")
    @CacheEvict(value = "price", key = "#id")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<PriceDto> deletePrice(
            @Parameter(description = "ID of the Price to delete") @PathVariable Long id) {
        priceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get Prices in Range", description = "Retrieve Prices within a specified price range")
    @Cacheable(value = "price", key = "'search_' + #minPrice + '_to_' + #maxPrice + '_page_' + #page + '_size_' + #size + '_asc_' + #asc")
    @GetMapping("/range")
    public ResponseEntity<PagedDataDto<PriceDto>> getPricesInRange(
            @Parameter(description = "Minimum price for the range") @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum price for the range") @RequestParam BigDecimal maxPrice,
            @Parameter(description = "Page number for pagination") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size for pagination") @RequestParam(required = false, defaultValue = "3") int size,
            @Parameter(description = "Sort in ascending order") boolean asc) {
        PagedDataDto<PriceDto> prices = priceService.getPricesInRange(minPrice, maxPrice, page, size, asc);
        return new ResponseEntity<>(prices, HttpStatus.OK);
    }

    @Operation(summary = "Search Prices", description = "Search for Prices by a keyword")
    @Cacheable(value = "price", key = "'search_' + #keyword + '_page_' + #page + '_size_' + #size")
    @GetMapping("/search")
    public ResponseEntity<PagedDataDto<PriceDto>> searchPrice(
            @Parameter(description = "Keyword to search Prices") @RequestParam String keyword,
            @Parameter(description = "Page number for pagination") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size for pagination") @RequestParam(required = false, defaultValue = "3") int size) {
        return new ResponseEntity<>(priceService.search(keyword, PageRequest.of(page, size)), HttpStatus.OK);
    }

    @Operation(summary = "Sort Prices", description = "Sort Prices by a specific attribute")
    @Cacheable(value = "price", key = "'sort_' + #keyword + '_reverse_' + #reverse + '_page_' + #page + '_size_' + #size")
    @GetMapping("/sort")
    public ResponseEntity<PagedDataDto<PriceDto>> sortPrice(
            @Parameter(description = "Attribute to sort by") @RequestParam String keyword,
            @Parameter(description = "Reverse sorting order") @RequestParam(defaultValue = "false") boolean reverse,
            @Parameter(description = "Page number for pagination") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size for pagination") @RequestParam(required = false, defaultValue = "6") int size) {
        Sort sort = Sort.by(reverse ? Sort.Direction.DESC : Sort.Direction.ASC, keyword);
        return new ResponseEntity<>(priceService.getAll(PageRequest.of(page, size, sort)), HttpStatus.OK);
    }
}
