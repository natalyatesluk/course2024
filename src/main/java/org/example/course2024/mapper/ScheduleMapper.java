package org.example.course2024.mapper;

import org.example.course2024.dto.ScheduleCreationDto;
import org.example.course2024.dto.ScheduleDto;
import org.example.course2024.entity.Schedule;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ScheduleMapper {
    @Mapping(source = "masterPhone", target = "master.phone")
    @Mapping(source = "masterSurname", target = "master.surname")
    @Mapping(source = "masterId", target = "master.id")
    Schedule toEntity(ScheduleDto scheduleDto);

    @InheritInverseConfiguration(name = "toEntity")
    ScheduleDto toDto(Schedule schedule);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Schedule partialUpdate(ScheduleDto scheduleDto, @MappingTarget Schedule schedule);

    @Mapping(source = "masterId", target = "master.id")
    Schedule toEntity(ScheduleCreationDto scheduleCreationDto);

    @Mapping(source = "master.id", target = "masterId")
    ScheduleCreationDto toDto1(Schedule schedule);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "masterId", target = "master.id")
    Schedule partialUpdate(ScheduleCreationDto scheduleCreationDto, @MappingTarget Schedule schedule);
}