package org.example.course2024.service;

import org.example.course2024.dto.CustomerDto;
import org.example.course2024.entity.Customer;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.example.course2024.exception.NotFoundException;
import org.example.course2024.mapper.CustomerMapper;
import org.example.course2024.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class CustomerService {
    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public CustomerDto getById(Long id) {
        Customer customer = customerRepository.findById(id).
                orElseThrow(()->new NotFoundException("Customer not found"));
        return customerMapper.toDto(customer);
    }

  public CustomerDto create(CustomerDto customerDto) {
        return customerMapper.toDto(customerRepository.save(customerMapper.toEntity(customerDto)));
  }

    @Transactional(readOnly = true)
    public List<CustomerDto> getAll(){
        return customerRepository.findAll().stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }

    public CustomerDto update(Long id, CustomerDto masterDto){
        Customer customer = customerRepository.findById(id).
                orElseThrow(()->new NotFoundException("Master not found"));
        customer.setName(masterDto.name());
        customer.setSurname(masterDto.surname());
        customer.setMiddleName(masterDto.middleName());
        customer.setPhone(masterDto.phone());
        customer.setPartBody(masterDto.partBody());
        return customerMapper.toDto(customerRepository.save(customer));
    }

    public void delete(Long id){
        customerRepository.deleteById(id);
    }
}
