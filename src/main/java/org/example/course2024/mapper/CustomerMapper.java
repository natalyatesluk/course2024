package org.example.course2024.mapper;

import org.example.course2024.dto.CustomerDto;
import org.example.course2024.dto.CustomerUpdatingDto;
import org.example.course2024.entity.Customer;
import org.example.course2024.dto.CustomerCreationDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {
    Customer toEntity(CustomerCreationDto customerCreationDto);

    Customer toEntity(CustomerDto customerDto);

    CustomerDto toDto(Customer customer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Customer partialUpdate(CustomerCreationDto customerCreationDto, @MappingTarget Customer customer);

    Customer toEntity(CustomerUpdatingDto customerUpdatingDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Customer partialUpdate(CustomerUpdatingDto customerUpdatingDto, @MappingTarget Customer customer);
}