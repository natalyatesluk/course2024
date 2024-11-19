package org.example.course2024.service;

import lombok.AllArgsConstructor;
import org.example.course2024.dto.AppointmentCreationDto;
import org.example.course2024.dto.AppointmentDto;
import org.example.course2024.dto.AppointmentUpdatingDto;
import org.example.course2024.dto.ScheduleDto;
import org.example.course2024.entity.Appointment;
import org.example.course2024.entity.Customer;
import org.example.course2024.entity.Master;
import org.example.course2024.entity.Schedule;
import org.example.course2024.enums.StatusAppoint;
import org.example.course2024.enums.StatusTime;
import org.example.course2024.exception.NotFoundException;
import org.example.course2024.exception.ScheduleAlreadyBusyException;
import org.example.course2024.mapper.AppointmentMapper;
import org.example.course2024.repository.AppointmentRepository;
import org.example.course2024.repository.CustomerRepository;
import org.example.course2024.repository.MasterRepository;
import org.example.course2024.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final CustomerRepository customerRepository;
    private final MasterRepository masterRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional(readOnly = true)
    public List<AppointmentDto> getAll(){
        return appointmentRepository.findAll().
                stream().map(appointmentMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AppointmentDto getById(Long id){
        return appointmentMapper.
                toDto(appointmentRepository.findById(id).orElseThrow(()->new NotFoundException("Appointment not found")));
    }

    public AppointmentDto create(AppointmentCreationDto appointmentDto){
        Customer customer = customerRepository.findById(appointmentDto.customerId()).orElseThrow(()->new NotFoundException("Customer not found"));
        Master master = masterRepository.findById(appointmentDto.masterId()).orElseThrow(()->new NotFoundException("Master not found"));
        Schedule schedule = scheduleRepository.findById(appointmentDto.scheduleId()).orElseThrow(()->new NotFoundException("Schedule not found"));
        Appointment appointment = appointmentMapper.toEntity(appointmentDto);

        appointment.setCustomer(customer);
        appointment.setMaster(master);
        appointment.setSchedule(schedule);
        appointmentRepository.save(appointment);
        return appointmentMapper.toDto(appointment);
    }

    public AppointmentDto update(AppointmentUpdatingDto appointmentDto, Long id){
        Appointment appointment= appointmentRepository.findById(id).orElseThrow(()->new NotFoundException("Appointment not found"));

        if(appointmentDto.id()!=null){
            appointment.setId(appointmentDto.id());
        }
        if(appointmentDto.masterId()!=null){
            Master master =masterRepository.findById(appointmentDto.masterId()).orElseThrow(()->new NotFoundException("Master not found"));
            appointment.setMaster(master);
        }
        if(appointmentDto.customerId()!=null){
            Customer customer = customerRepository.findById(appointmentDto.customerId()).orElseThrow(()->new NotFoundException("Customer not found"));
            appointment.setCustomer(customer);
        }
        if(appointmentDto.status()!=null){
            appointment.setStatus(appointmentDto.status());
            if(appointmentDto.status()== StatusAppoint.CANCELLED){
                Schedule schedule = scheduleRepository.findById(appointment.getId()).orElseThrow(()->new NotFoundException("Schedule not found"));
                schedule.setStatus(StatusTime.FREE);
            }
        }
        if(appointmentDto.scheduleId()!=null){
            if(!appointmentDto.scheduleId().equals(appointment.getSchedule().getId())){
                Schedule scheduleOld = scheduleRepository.findById(appointment.getSchedule().getId())
                        .orElseThrow(() -> new NotFoundException("Schedule not found"));
                Schedule scheduleNew = scheduleRepository.findById(appointmentDto.scheduleId()).orElseThrow(()->new NotFoundException("Schedule not found"));
                if (scheduleNew.getStatus() == StatusTime.BUSY) {
                    throw new ScheduleAlreadyBusyException("The selected schedule is already marked as busy.");
                }
                scheduleOld.setStatus(StatusTime.FREE);
                scheduleRepository.save(scheduleOld);
                scheduleNew.setStatus(StatusTime.BUSY);
                scheduleRepository.save(scheduleNew);
                appointment.setSchedule(scheduleNew);
            }
        }

        appointmentRepository.save(appointment);
        return appointmentMapper.toDto(appointment);
    }

    public void delete(Long id){
        appointmentRepository.findById(id).orElseThrow(()->new NotFoundException("Appointment not found"));
    }
    public Map<StatusAppoint, Long> getStatusCounts() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .collect(Collectors.groupingBy(Appointment::getStatus, Collectors.counting()));
    }
}

