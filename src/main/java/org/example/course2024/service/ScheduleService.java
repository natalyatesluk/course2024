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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public PagedDataDto<ScheduleDto> getAll(int page, int size, boolean asc) {
        Sort sort = Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Schedule> schedulesPage = scheduleRepository.findAll(pageable);

        return toPagedDataDto(schedulesPage);
    }

    @Transactional(readOnly = true)
    public PagedDataDto<ScheduleDto> getByDateRange(LocalDate startDate, LocalDate endDate, int page, int size, boolean asc) {
        Sort sort = Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Schedule> schedulesPage = scheduleRepository.findByDateRange(startDate, endDate, pageable);

        return toPagedDataDto(schedulesPage);
    }

    @Transactional(readOnly = true)
    public PagedDataDto<ScheduleDto> getByDateTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime, int page, int size, boolean asc) {
        Sort sort = Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, "dateTime");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Schedule> schedulesPage = scheduleRepository.findByDateTimeRange(startDateTime, endDateTime, pageable);

        return toPagedDataDto(schedulesPage);
    }
    public List<ScheduleDto> getPriceByMaster(Long idMaster){

        List <Schedule> prices= scheduleRepository.findByMaster(idMaster);
        return prices.stream().map(scheduleMapper::toDto).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public PagedDataDto<ScheduleDto> getStatusList(String status, int page, int size, boolean asc) {
        Sort sort = Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, "date");
        Pageable pageable = PageRequest.of(page, size, sort);

        StatusTime statusTime = StatusTime.valueOf(status.toUpperCase());
        Page<Schedule> schedulesPage = scheduleRepository.findByStatus(statusTime, pageable);

        return toPagedDataDto(schedulesPage);
    }

    private PagedDataDto<ScheduleDto> toPagedDataDto(Page<Schedule> schedulesPage) {
        List<ScheduleDto> schedules = schedulesPage.getContent().stream()
                .map(scheduleMapper::toDto)
                .collect(Collectors.toList());

        return new PagedDataDto<>(
                schedules,
                schedulesPage.getNumber(),
                schedulesPage.getSize(),
                schedulesPage.getTotalElements(),
                schedulesPage.getTotalPages()
        );
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

    public ScheduleDto update(Long id, ScheduleUpdatingDto scheduleDto) {
        Schedule existingSchedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Schedule not found"));

        if(scheduleDto.id() != null){
            existingSchedule.setId(scheduleDto.id());
        }
        if (scheduleDto.masterId() != null) {
            Master master = masterRepository.findById(scheduleDto.masterId())
                    .orElseThrow(() -> new NotFoundException("Master not found"));
            existingSchedule.setMaster(master);
        }

        if (scheduleDto.status() != null) {
            existingSchedule.setStatus(scheduleDto.status());
        }

        if (scheduleDto.date() != null) {
            existingSchedule.setDate(scheduleDto.date());
        }

        return scheduleMapper.toDto(scheduleRepository.save(existingSchedule));
    }

    public void delete(Long id) {
        scheduleRepository.deleteById(id);
    }


    }

//    @Transactional(readOnly = true)
//    public List<ScheduleDto> getByTimeRange(LocalTime startTime, LocalTime endTime) {
//        return scheduleRepository.findByTimeRange(startTime, endTime)
//                .stream()
//                .map(scheduleMapper::toDto)
//                .collect(Collectors.toList());
//    }



