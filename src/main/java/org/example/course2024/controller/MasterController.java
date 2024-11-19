package org.example.course2024.controller;

import org.example.course2024.dto.CustomerDto;
import org.example.course2024.dto.MasterDto;
import org.example.course2024.dto.PagedDataDto;
import org.example.course2024.service.MasterService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/masters")
public class MasterController {
    private final MasterService masterService;

    public MasterController(MasterService masterService) {
        this.masterService = masterService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MasterDto> getMaster(@PathVariable Long id) {
        MasterDto masterDto = masterService.getById(id);
        return new ResponseEntity<>(masterDto, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<PagedDataDto<MasterDto>> getAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "3") int size
    ) {
        return new ResponseEntity<>(masterService.getAll(PageRequest.of(page, size)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MasterDto> createMaster(@RequestBody MasterDto masterDto) {
        MasterDto createdMaster = masterService.create(masterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMaster);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MasterDto> updateMaster(@PathVariable Long id, @RequestBody MasterDto masterDto) {
        MasterDto updatedMaster = masterService.update(id, masterDto);
        return ResponseEntity.ok(updatedMaster);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaster(@PathVariable Long id) {
        masterService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<PagedDataDto<MasterDto>> searchCustomer(@RequestParam String keyword,
                                                          @RequestParam(required = false, defaultValue = "0") int page,
                                                          @RequestParam(required = false, defaultValue = "3") int size) {
        return new ResponseEntity<>(masterService.search(keyword, PageRequest.of(page, size)), HttpStatus.OK);
    }

    @GetMapping("/sort")
    public ResponseEntity<PagedDataDto<MasterDto>> sortCustomer(@RequestParam String keyword,
                                                        @RequestParam(defaultValue = "false") boolean reverse,
                                                        @RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "3") int size) {

        Sort sort = Sort.by(reverse ? Sort.Direction.DESC : Sort.Direction.ASC, keyword);
        return new ResponseEntity<>(masterService.getAll(PageRequest.of(page,size, sort)), HttpStatus.OK);
    }
}
