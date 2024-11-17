package org.example.course2024.controller;

import org.example.course2024.dto.CustomerDto;
import org.example.course2024.entity.Customer;
import org.example.course2024.service.CustomerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id) {
        return new ResponseEntity<CustomerDto>(customerService.getById(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return new ResponseEntity<>(customerService.getAll(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(customerDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id,@RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(customerService.update(id,customerDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomerDto> deleteCustomer(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerDto>> searchCustomer(@RequestParam String keyword) {
        return new ResponseEntity<>(customerService.search(keyword), HttpStatus.OK);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<CustomerDto>> sortCustomer(@RequestParam String keyword,
                                                          @RequestParam(defaultValue = "false") boolean reverse) {
        return new ResponseEntity<>(customerService.sorted(keyword, reverse), HttpStatus.OK);
    }
}
