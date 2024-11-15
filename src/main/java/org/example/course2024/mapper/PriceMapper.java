package org.example.course2024.mapper;

import org.example.course2024.dto.PriceCreationDto;
import org.example.course2024.dto.PriceDto;
import org.example.course2024.entity.Price;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PriceMapper {
    @Mapping(source = "masterSurname", target = "master.surname")
    @Mapping(source = "masterName", target = "master.name")
    @Mapping(source = "masterId", target = "master.id")
    Price toEntity(PriceDto priceDto);

    @InheritInverseConfiguration(name = "toEntity")
    PriceDto toDto(Price price);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Price partialUpdate(PriceDto priceDto, @MappingTarget Price price);

    @Mapping(source = "masterId", target = "master.id")
    Price toEntity(PriceCreationDto priceCreationDto);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "masterId", target = "master.id")
    Price partialUpdate(PriceCreationDto priceCreationDto, @MappingTarget Price price);
}