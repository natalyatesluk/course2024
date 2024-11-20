package org.example.course2024.controller;

import org.example.course2024.dto.CustomerCreationDto;
import org.example.course2024.dto.CustomerDto;
import org.example.course2024.dto.CustomerUpdatingDto;
import org.example.course2024.dto.PagedDataDto;
import org.example.course2024.service.CustomerService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Cacheable(value = "customers", key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id) {
        return new ResponseEntity<>(customerService.getById(id), HttpStatus.OK);
    }

    @Cacheable(value = "customers", key = "'page_' + #page + '_size_' + #size")
    @GetMapping()
    public ResponseEntity<PagedDataDto<CustomerDto>> getAllCustomers(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "3") int size
    ) {
        return new ResponseEntity<>(customerService.getAll(PageRequest.of(page, size)), HttpStatus.OK);
    }

    @CachePut(value = "customers")
    @PostMapping()
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerCreationDto customerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(customerDto));
    }

    @CachePut(value = "customers", key = "#id")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id, @RequestBody CustomerUpdatingDto customerDto) {
        return ResponseEntity.ok(customerService.update(id, customerDto));
    }

    @CacheEvict(value = "customers", key = "#id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Cacheable(value = "customers", key = "'search_' + #keyword + '_page_' + #page + '_size_' + #size")
    @GetMapping("/search")
    public ResponseEntity<PagedDataDto<CustomerDto>> searchCustomer(@RequestParam String keyword,
                                                                    @RequestParam(required = false, defaultValue = "0") int page,
                                                                    @RequestParam(required = false, defaultValue = "3") int size) {
        return new ResponseEntity<>(customerService.search(keyword, PageRequest.of(page, size)), HttpStatus.OK);
    }

    @Cacheable(value = "customers", key = "'sort_' + #keyword + '_reverse_' + #reverse + '_page_' + #page + '_size_' + #size")
    @GetMapping("/sort")
    public ResponseEntity<PagedDataDto<CustomerDto>> sortCustomer(@RequestParam String keyword,
                                                                  @RequestParam(defaultValue = "false") boolean reverse,
                                                                  @RequestParam(required = false, defaultValue = "0") int page,
                                                                  @RequestParam(required = false, defaultValue = "3") int size) {
        Sort sort = Sort.by(reverse ? Sort.Direction.DESC : Sort.Direction.ASC, keyword);
        return new ResponseEntity<>(customerService.getAll(PageRequest.of(page, size, sort)), HttpStatus.OK);
    }
}
