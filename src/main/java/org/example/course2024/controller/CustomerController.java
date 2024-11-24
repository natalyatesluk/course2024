package org.example.course2024.controller;

import jakarta.validation.Valid;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer API", description = "Operations for managing Customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Get Customer by ID", description = "Retrieve a single Customer by their unique ID")
    @Cacheable(value = "customers", key = "#id")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(
            @Parameter(description = "ID of the Customer to retrieve") @PathVariable Long id) {
        return ResponseEntity.ok(customerService.getById(id));
    }

    @Operation(summary = "Get all Customers", description = "Retrieve a paginated list of Customers")
    @Cacheable(value = "customers", key = "'page_' + #page + '_size_' + #size")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PagedDataDto<CustomerDto>> getAllCustomers(
            @Parameter(description = "Page number for pagination") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size for pagination") @RequestParam(defaultValue = "3") int size) {
        return ResponseEntity.ok(customerService.getAll(PageRequest.of(page, size)));
    }

    @Operation(summary = "Create a new Customer", description = "Add a new Customer to the system")
    @CachePut(value = "customers")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(
            @Valid @RequestBody CustomerCreationDto customerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(customerDto));
    }

    @Operation(summary = "Update Customer by ID", description = "Update details of an existing Customer by their ID")
    @CachePut(value = "customers", key = "#id")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(
            @Parameter(description = "ID of the Customer to update") @PathVariable Long id,
            @Valid @RequestBody CustomerUpdatingDto customerDto) {
        return ResponseEntity.ok(customerService.update(id, customerDto));
    }

    @Operation(summary = "Delete Customer by ID", description = "Delete an existing Customer by their unique ID")
    @CacheEvict(value = "customers", key = "#id")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "ID of the Customer to delete") @PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search Customers", description = "Search for Customers by a keyword")
    @Cacheable(value = "customers", key = "'search_' + #keyword + '_page_' + #page + '_size_' + #size")
    @GetMapping("/search")
    public ResponseEntity<PagedDataDto<CustomerDto>> searchCustomer(
            @Parameter(description = "Keyword to search Customers") @RequestParam String keyword,
            @Parameter(description = "Page number for pagination") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size for pagination") @RequestParam(defaultValue = "3") int size) {
        return ResponseEntity.ok(customerService.search(keyword, PageRequest.of(page, size)));
    }

    @Operation(summary = "Sort Customers", description = "Sort Customers by a specific attribute")
    @Cacheable(value = "customers", key = "'sort_' + #keyword + '_reverse_' + #reverse + '_page_' + #page + '_size_' + #size")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sort")
    public ResponseEntity<PagedDataDto<CustomerDto>> sortCustomer(
            @Parameter(description = "Attribute to sort by") @RequestParam String keyword,
            @Parameter(description = "Reverse sorting order") @RequestParam(defaultValue = "false") boolean reverse,
            @Parameter(description = "Page number for pagination") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size for pagination") @RequestParam(defaultValue = "3") int size) {
        Sort sort = Sort.by(reverse ? Sort.Direction.DESC : Sort.Direction.ASC, keyword);
        return ResponseEntity.ok(customerService.getAll(PageRequest.of(page, size, sort)));
    }

    @Operation(summary = "Get Customer's appointments list", description = "Retrieve a list of appointments related to a Customer by ID")
    @Cacheable(value = "customers", key = "#id + '_appointments'")
    @GetMapping("/{id}/appointments")
    public ResponseEntity<?> getCustomerAppointmentsById(
            @Parameter(description = "ID of the Customer") @PathVariable Long id) {
        Object appointments = customerService.getAppointmentsByCustomerId(id);
        return ResponseEntity.ok(appointments);
    }
    private boolean hasRole(String role) {
        // Check the authentication object for the user's role
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
    }
}
