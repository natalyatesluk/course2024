package org.example.course2024.mapper;

import org.example.course2024.dto.AppointmentCreationDto;
import org.example.course2024.dto.AppointmentDto;
import org.example.course2024.dto.AppointmentUpdatingDto;
import org.example.course2024.entity.Appointment;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppointmentMapper {

    @Mapping(source = "masterId", target = "master.id")
    @Mapping(source = "scheduleId", target = "schedule.id")
    @Mapping(source = "customerId", target = "customer.id")
    Appointment toEntity(AppointmentCreationDto appointmentCreationDto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "customer.surname", target = "customerSurname")
    @Mapping(source = "customer.phone", target = "customerPhone")
    @Mapping(source = "customer.email", target = "customerEmail")
    @Mapping(source = "customer.partBody", target = "customerPartBody")
    @Mapping(source = "schedule.date", target = "scheduleDate")
    @Mapping(source = "master.surname", target = "masterSurname")
    @Mapping(source = "master.phone", target = "masterPhone")
    @Mapping(source = "master.email", target = "masterEmail")
    AppointmentDto toDto(Appointment appointment);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "masterId", target = "master.id")
    @Mapping(source = "scheduleId", target = "schedule.id")
    @Mapping(source = "customerId", target = "customer.id")
    @Mapping(source = "localDateTime", target = "schedule.date")
    Appointment toEntity(AppointmentUpdatingDto appointmentUpdatingDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Appointment partialUpdate(AppointmentCreationDto appointmentCreationDto, @MappingTarget Appointment appointment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Appointment partialUpdate(AppointmentUpdatingDto appointmentUpdatingDto, @MappingTarget Appointment appointment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Appointment partialUpdate(AppointmentDto appointmentDto, @MappingTarget Appointment appointment);
}
