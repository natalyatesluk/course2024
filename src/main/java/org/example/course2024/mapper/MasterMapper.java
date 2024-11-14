package org.example.course2024.mapper;

import org.example.course2024.dto.MasterCreationDto;
import org.example.course2024.dto.MasterDto;
import org.example.course2024.entity.Master;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MasterMapper {

    Master toEntity(MasterDto masterDto);

    Master toEntity(MasterCreationDto masterCreationDto);

    MasterDto toDto(Master master);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Master partialUpdate(MasterCreationDto masterCreationDto, @MappingTarget Master master);

}