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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/masters")
public class MasterController {
    private final MasterService masterService;

    public MasterController(MasterService masterService) {
        this.masterService = masterService;
    }

    @Cacheable(value = "masters", key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<MasterDto> getMaster(@PathVariable Long id) {
        MasterDto masterDto = masterService.getById(id);
        return new ResponseEntity<>(masterDto, HttpStatus.OK);
    }

    @Cacheable(value = "masters", key = "'page_' + #page + '_size_' + #size")
    @GetMapping("")
    public ResponseEntity<PagedDataDto<MasterDto>> getAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "3") int size
    ) {
        return new ResponseEntity<>(masterService.getAll(PageRequest.of(page, size)), HttpStatus.OK);
    }

    @CachePut(value = "masters")
    @PostMapping
    public ResponseEntity<MasterDto> createMaster(@Valid @RequestBody MasterCreationDto masterDto) {
        MasterDto createdMaster = masterService.create(masterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMaster);
    }

    @CachePut(value = "masters", key = "#id")
    @PutMapping("/{id}")
    public ResponseEntity<MasterDto> updateMaster(@Valid  @PathVariable Long id, @RequestBody MasterUpdatingDto masterDto) {
        MasterDto updatedMaster = masterService.update(id, masterDto);
        return ResponseEntity.ok(updatedMaster);
    }

    @CacheEvict(value = "masters", key = "#id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaster(@PathVariable Long id) {
        masterService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Cacheable(value = "masters", key = "'search_' + #keyword + '_page_' + #page + '_size_' + #size")
    @GetMapping("/search")
    public ResponseEntity<PagedDataDto<MasterDto>> searchCustomer(@RequestParam String keyword,
                                                                  @RequestParam(required = false, defaultValue = "0") int page,
                                                                  @RequestParam(required = false, defaultValue = "3") int size) {
        return new ResponseEntity<>(masterService.search(keyword, PageRequest.of(page, size)), HttpStatus.OK);
    }

    @Cacheable(value = "masters", key = "'sort_' + #keyword + '_reverse_' + #reverse + '_page_' + #page + '_size_' + #size")
    @GetMapping("/sort")
    public ResponseEntity<PagedDataDto<MasterDto>> sortCustomer(@RequestParam String keyword,
                                                                @RequestParam(defaultValue = "false") boolean reverse,
                                                                @RequestParam(required = false, defaultValue = "0") int page,
                                                                @RequestParam(required = false, defaultValue = "3") int size) {

        Sort sort = Sort.by(reverse ? Sort.Direction.DESC : Sort.Direction.ASC, keyword);
        return new ResponseEntity<>(masterService.getAll(PageRequest.of(page, size, sort)), HttpStatus.OK);
    }
}
