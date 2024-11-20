package org.example.course2024.service;

import org.example.course2024.dto.CustomerCreationDto;
import org.example.course2024.dto.CustomerDto;
import org.example.course2024.entity.Customer;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.example.course2024.exception.NotFoundException;
import org.example.course2024.mapper.CustomerMapper;
import org.example.course2024.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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
                orElseThrow(() -> new NotFoundException("Customer not found"));
        return customerMapper.toDto(customer);
    }

  public CustomerDto create(CustomerCreationDto customerDto) {
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

    public  List<CustomerDto> search(String keyword){
        List<Customer> customers = customerRepository.findAll().stream()
                .filter(customer ->
                        (customer.getPhone() != null && customer.getPhone().contains(keyword)) ||
                                (customer.getName() != null && customer.getName().toLowerCase().contains(keyword.toLowerCase())) ||
                                (customer.getSurname() != null && customer.getSurname().toLowerCase().contains(keyword.toLowerCase()))||
                                (customer.getPartBody() != null && customer.getPartBody().toString().contains(keyword.toUpperCase()))
                )
                .collect(Collectors.toList());

            return customers.stream().map(customer -> customerMapper.toDto(customer)).collect(Collectors.toList());
        }

    public List<CustomerDto> sorted(String keyword, boolean reverse) {
        List<Customer> customers = customerRepository.findAll();

        List<Customer> sortedCustomers;
        if (keyword.equalsIgnoreCase("name")) {
            sortedCustomers = customers.stream()
                    .sorted(reverse? Comparator.comparing(Customer::getName).reversed()
                            :Comparator.comparing(Customer::getName))
                    .collect(Collectors.toList());
        } else if (keyword.equalsIgnoreCase("surname")) {
            sortedCustomers = customers.stream()
                    .sorted(reverse? Comparator.comparing(Customer::getSurname).reversed()
                            :Comparator.comparing(Customer::getSurname))
                    .collect(Collectors.toList());
        } else if (keyword.equalsIgnoreCase("phone")) {
            sortedCustomers = customers.stream()
                    .sorted(reverse? Comparator.comparing(Customer::getPhone).reversed()
                            :Comparator.comparing(Customer::getPhone))
                    .collect(Collectors.toList());
        } else {
            sortedCustomers = customers;
        }

        return sortedCustomers.stream()
                .map(customer -> customerMapper.toDto(customer))
                .collect(Collectors.toList());
    }
    }

