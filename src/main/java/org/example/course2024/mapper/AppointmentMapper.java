package org.example.course2024.mapper;

import org.example.course2024.dto.AppointmentCreationDto;
import org.example.course2024.dto.AppointmentDto;
import org.example.course2024.entity.Appointment;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppointmentMapper {
    @Mapping(source = "masterPhone", target = "master.phone")
    @Mapping(source = "masterSurname", target = "master.surname")
    @Mapping(source = "masterId", target = "master.id")
    @Mapping(source = "scheduleDate", target = "schedule.date")
    @Mapping(source = "customerPartBody", target = "customer.partBody")
    @Mapping(source = "customerPhone", target = "customer.phone")
    @Mapping(source = "customerSurname", target = "customer.surname")
    Appointment toEntity(AppointmentDto appointmentDto);

    @InheritInverseConfiguration(name = "toEntity")
    AppointmentDto toDto(Appointment appointment);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Appointment partialUpdate(AppointmentDto appointmentDto, @MappingTarget Appointment appointment);

    @Mapping(source = "masterId", target = "master.id")
    @Mapping(source = "scheduleId", target = "schedule.id")
    @Mapping(source = "customerId", target = "customer.id")
    Appointment toEntity(AppointmentCreationDto appointmentCreationDto);


    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Appointment partialUpdate(AppointmentCreationDto appointmentCreationDto, @MappingTarget Appointment appointment);
}