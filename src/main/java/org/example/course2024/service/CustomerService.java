package org.example.course2024.service;

import org.example.course2024.dto.*;
import org.example.course2024.entity.Customer;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.example.course2024.exception.NotFoundException;
import org.example.course2024.mapper.CustomerMapper;
import org.example.course2024.repository.CustomerRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class CustomerService {
    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    private final AppointmentService appointmentService;

    @Transactional(readOnly = true)
    public CustomerDto getById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        return customerMapper.toDto(customer);
    }

    public CustomerDto create(CustomerCreationDto customerDto) {
        return customerMapper.toDto(customerRepository.save(customerMapper.toEntity(customerDto)));
    }

    @Transactional(readOnly = true)
    public PagedDataDto<CustomerDto> getAll(PageRequest pageRequest) {
        Page<Customer> customers = customerRepository.findAll(pageRequest);
        List<CustomerDto> data = customers.getContent().stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
        PagedDataDto<CustomerDto> pageAllCustomers = new PagedDataDto<>();
        pageAllCustomers.setData(data);
        pageAllCustomers.setPage(customers.getNumber());
        pageAllCustomers.setTotal(customers.getTotalElements());
        pageAllCustomers.setTotalPages(customers.getTotalPages());
        pageAllCustomers.setPageSize(customers.getSize());
        return pageAllCustomers;
    }

    public CustomerDto update(Long id, CustomerUpdatingDto customerUpdatingDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        if (customerUpdatingDto.name() != null) customer.setName(customerUpdatingDto.name());
        if (customerUpdatingDto.surname() != null) customer.setSurname(customerUpdatingDto.surname());
        if (customerUpdatingDto.middleName() != null) customer.setMiddleName(customerUpdatingDto.middleName());
        if (customerUpdatingDto.phone() != null) customer.setPhone(customerUpdatingDto.phone());
        if (customerUpdatingDto.email() != null) customer.setEmail(customerUpdatingDto.email());
        if (customerUpdatingDto.partBody() != null) customer.setPartBody(customerUpdatingDto.partBody());

        return customerMapper.toDto(customerRepository.save(customer));
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    public PagedDataDto<CustomerDto> search(String keyword, PageRequest pageRequest) {
        List<Customer> filteredCustomers = customerRepository.findAll().stream()
                .filter(customer -> (customer.getPhone() != null && customer.getPhone().contains(keyword)) ||
                        (customer.getName() != null && customer.getName().toLowerCase().contains(keyword.toLowerCase())) ||
                        (customer.getSurname() != null && customer.getSurname().toLowerCase().contains(keyword.toLowerCase())) ||
                        (customer.getPartBody() != null && customer.getPartBody().toString().contains(keyword.toUpperCase())))
                .collect(Collectors.toList());

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), filteredCustomers.size());
        List<Customer> pageContent = filteredCustomers.subList(start, end);
        Page<Customer> customersPage = new PageImpl<>(pageContent, pageRequest, filteredCustomers.size());

        List<CustomerDto> content = filteredCustomers.stream().map(customerMapper::toDto).collect(Collectors.toList());

        PagedDataDto<CustomerDto> allCustomerPage = new PagedDataDto<>();
        allCustomerPage.setData(content);
        allCustomerPage.setPage(customersPage.getNumber());
        allCustomerPage.setTotal(customersPage.getTotalElements());
        allCustomerPage.setTotalPages(customersPage.getTotalPages());
        allCustomerPage.setPageSize(customersPage.getSize());
        return  allCustomerPage;
    }

    public Object getAppointmentsByCustomerId(Long id) {
        return  appointmentService.getByCustomer(id);
    }

//    public PagedDataDto<CustomerDto> sortCustomer(String keyword, boolean reverse, PageRequest pageRequest) {
//        Sort sort = Sort.by(reverse ? Sort.Direction.DESC : Sort.Direction.ASC, keyword);
//        return getAll(PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sort));
//    }
}
