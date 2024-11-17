package org.example.course2024.service;


import lombok.AllArgsConstructor;
import org.example.course2024.dto.*;
import org.example.course2024.entity.Master;
import org.example.course2024.entity.Price;
import org.example.course2024.entity.Schedule;
import org.example.course2024.enums.StatusTime;
import org.example.course2024.exception.NotFoundException;
import org.example.course2024.mapper.PriceMapperImpl;
import org.example.course2024.mapper.ScheduleMapper;
import org.example.course2024.repository.MasterRepository;
import org.example.course2024.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ScheduleService {

    private ScheduleRepository scheduleRepository;
    private ScheduleMapper scheduleMapper;
    private final MasterRepository masterRepository;

    @Transactional(readOnly = true)
    public List<ScheduleDto> getAll(){
        return scheduleRepository.findAll().stream().map(scheduleMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ScheduleDto getById(Long id){
        return scheduleMapper.toDto(scheduleRepository.findById(id).orElseThrow(() -> new NotFoundException("Time not found")));
    }
    public ScheduleDto create(ScheduleCreationDto scheduleCreationDto) {
        Master master = masterRepository.findById(scheduleCreationDto.masterId())
                .orElseThrow(() -> new NotFoundException("Master not found"));

        Schedule schedule = scheduleMapper.toEntity(scheduleCreationDto);
        schedule.setMaster(master);
        return scheduleMapper.toDto(scheduleRepository.save(schedule));
    }

    public ScheduleDto update(Long id, ScheduleCreationDto scheduleDto) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new NotFoundException("Price not found"));
        schedule.setDate(scheduleDto.date());
        schedule.setStatus(scheduleDto.status());
        Master master = masterRepository.findById(scheduleDto.masterId()).orElseThrow(() -> new NotFoundException("Master not found"));
        schedule.setMaster(master);
        return scheduleMapper.toDto(scheduleRepository.save(schedule));
    }

    public void delete(Long id) {
        scheduleRepository.deleteById(id);
    }

//    @Transactional(readOnly = true)
//    public List<ScheduleDto> getByDateRange(LocalDate startDate, LocalDate endDate) {
//        return scheduleRepository.findByDateRange(startDate, endDate)
//                .stream()
//                .map(scheduleMapper::toDto)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public List<ScheduleDto> getByTimeRange(LocalTime startTime, LocalTime endTime) {
//        return scheduleRepository.findByTimeRange(startTime, endTime)
//                .stream()
//                .map(scheduleMapper::toDto)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public List<ScheduleDto> getByDateTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
//        return scheduleRepository.findByDateTimeRange(startDateTime, endDateTime)
//                .stream()
//                .map(scheduleMapper::toDto)
//                .collect(Collectors.toList());
//    }
    @Transactional(readOnly = true)
    public List<ScheduleDto> getStatusList(String status){

        try {
            StatusTime statusTime = StatusTime.valueOf(status.toUpperCase());

            List<Schedule> schedules = scheduleRepository.findByStatus(statusTime);

            return schedules.stream().map(scheduleMapper::toDto).collect(Collectors.toList());

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

    }
}
