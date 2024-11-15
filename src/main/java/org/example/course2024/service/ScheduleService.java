package org.example.course2024.service;


import lombok.AllArgsConstructor;
import org.example.course2024.dto.PriceCreationDto;
import org.example.course2024.dto.PriceDto;
import org.example.course2024.dto.ScheduleCreationDto;
import org.example.course2024.dto.ScheduleDto;
import org.example.course2024.entity.Master;
import org.example.course2024.entity.Price;
import org.example.course2024.entity.Schedule;
import org.example.course2024.exception.NotFoundException;
import org.example.course2024.mapper.PriceMapperImpl;
import org.example.course2024.mapper.ScheduleMapper;
import org.example.course2024.repository.MasterRepository;
import org.example.course2024.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ScheduleDto update(Long id, ScheduleDto scheduleDto) {
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
}
