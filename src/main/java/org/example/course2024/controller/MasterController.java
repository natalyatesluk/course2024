package org.example.course2024.controller;

import jakarta.validation.Valid;
import org.example.course2024.dto.MasterCreationDto;
import org.example.course2024.dto.MasterDto;
import org.example.course2024.dto.MasterUpdatingDto;
import org.example.course2024.dto.PagedDataDto;
import org.example.course2024.service.MasterService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/masters")
@Tag(name = "Master API", description = "Operations related to Master management")
public class MasterController {
    private final MasterService masterService;

    public MasterController(MasterService masterService) {
        this.masterService = masterService;
    }

    @Operation(summary = "Get Master by ID", description = "Retrieve a single Master by their unique ID")
    @Cacheable(value = "masters", key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<MasterDto> getMaster(
            @Parameter(description = "ID of the Master to retrieve") @PathVariable Long id) {
        MasterDto masterDto = masterService.getById(id);
        return new ResponseEntity<>(masterDto, HttpStatus.OK);
    }

    @Operation(summary = "Get all Masters", description = "Retrieve a paginated list of Masters")
    @Cacheable(value = "masters", key = "'page_' + #page + '_size_' + #size")
    @GetMapping("")
    public ResponseEntity<PagedDataDto<MasterDto>> getAll(
            @Parameter(description = "Page number for pagination") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size for pagination") @RequestParam(required = false, defaultValue = "3") int size) {
        return new ResponseEntity<>(masterService.getAll(PageRequest.of(page, size)), HttpStatus.OK);
    }

    @Operation(summary = "Create a new Master", description = "Add a new Master to the system")
    @CachePut(value = "masters")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MasterDto> createMaster(
            @Valid @RequestBody MasterCreationDto masterDto) {
        MasterDto createdMaster = masterService.create(masterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMaster);
    }

    @Operation(summary = "Update Master by ID", description = "Update details of an existing Master by their ID")
    @CachePut(value = "masters", key = "#id")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MasterDto> updateMaster(
            @Parameter(description = "ID of the Master to update") @PathVariable Long id,
            @Valid @RequestBody MasterUpdatingDto masterDto) {
        MasterDto updatedMaster = masterService.update(id, masterDto);
        return ResponseEntity.ok(updatedMaster);
    }

    @Operation(summary = "Delete Master by ID", description = "Delete an existing Master by their unique ID")
    @CacheEvict(value = "masters", key = "#id")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaster(
            @Parameter(description = "ID of the Master to delete") @PathVariable Long id) {
        masterService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search Masters", description = "Search for Masters by a keyword")
    @Cacheable(value = "masters", key = "'search_' + #keyword + '_page_' + #page + '_size_' + #size")
    @GetMapping("/search")
    public ResponseEntity<PagedDataDto<MasterDto>> searchCustomer(
            @Parameter(description = "Keyword to search Masters") @RequestParam String keyword,
            @Parameter(description = "Page number for pagination") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size for pagination") @RequestParam(required = false, defaultValue = "3") int size) {
        return new ResponseEntity<>(masterService.search(keyword, PageRequest.of(page, size)), HttpStatus.OK);
    }

    @Operation(summary = "Sort Masters", description = "Sort Masters by a specific attribute")
    @Cacheable(value = "masters", key = "'sort_' + #keyword + '_reverse_' + #reverse + '_page_' + #page + '_size_' + #size")
    @GetMapping("/sort")
    public ResponseEntity<PagedDataDto<MasterDto>> sortCustomer(
            @Parameter(description = "Attribute to sort by") @RequestParam String keyword,
            @Parameter(description = "Reverse sorting order") @RequestParam(defaultValue = "false") boolean reverse,
            @Parameter(description = "Page number for pagination") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size for pagination") @RequestParam(required = false, defaultValue = "3") int size) {
        Sort sort = Sort.by(reverse ? Sort.Direction.DESC : Sort.Direction.ASC, keyword);
        return new ResponseEntity<>(masterService.getAll(PageRequest.of(page, size, sort)), HttpStatus.OK);
    }

    @Operation(summary = "Get Master's specific list", description = "Retrieve a specific list (e.g., appointments) related to a Master by ID")
    @Cacheable(value = "masters", key = "#id + '_' + #listName")
    @GetMapping("/{id}/{listName}")
    public ResponseEntity<?> getMasterListById(
            @Parameter(description = "ID of the Master") @PathVariable Long id,
            @Parameter(description = "Name of the list to retrieve (e.g., appointments, schedules, etc.)") @PathVariable String listName) {
        if ("appointment".equalsIgnoreCase(listName)) {
            // Only ADMIN can access appointments
            if (!hasRole("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        Object list = masterService.getListByMasterIdAndName(id, listName);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    private boolean hasRole(String role) {
        // Check the authentication object for the user's role
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
    }
}

