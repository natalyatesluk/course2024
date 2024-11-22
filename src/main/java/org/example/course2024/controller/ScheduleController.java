package org.example.course2024.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.course2024.dto.PagedDataDto;
import org.example.course2024.dto.ScheduleCreationDto;
import org.example.course2024.dto.ScheduleDto;
import org.example.course2024.dto.ScheduleUpdatingDto;
import org.example.course2024.service.ScheduleService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/schedule")
@AllArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping()
    @Cacheable(value = "schedules", key = "{#page, #size, #asc}")
    public ResponseEntity<PagedDataDto<ScheduleDto>> getSchedules(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean asc) {
        return new ResponseEntity<>(scheduleService.getAll(page, size, asc), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Cacheable(value = "scheduleById", key = "#id")
    public ResponseEntity<ScheduleDto> getSchedule(@PathVariable Long id) {
        return new ResponseEntity<>(scheduleService.getById(id), HttpStatus.OK);
    }

    @PostMapping()
    @CacheEvict(value = {"schedules"}, allEntries = true)
    public ResponseEntity<ScheduleDto> createSchedule(@Valid  @RequestBody ScheduleCreationDto scheduleDto) {
        return new ResponseEntity<>(scheduleService.create(scheduleDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @CacheEvict(value = {"schedules", "scheduleById"}, allEntries = true)
    public ResponseEntity<ScheduleDto> updateSchedule(@Valid @PathVariable Long id, @RequestBody ScheduleUpdatingDto scheduleDto) {
        return new ResponseEntity<>(scheduleService.update(id, scheduleDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = {"schedules", "scheduleById"}, allEntries = true)
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/date-range")
    @Cacheable(value = "schedules", key = "{#startDate, #endDate, #page, #size, #asc}")
    public ResponseEntity<PagedDataDto<ScheduleDto>> getSchedulesByDateRange(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean asc) {
        return new ResponseEntity<>(scheduleService.getByDateRange(startDate, endDate, page, size, asc), HttpStatus.OK);
    }

    @GetMapping("/timeDateRange")
    @Cacheable(value = "schedules", key = "{#startDateTime, #endDateTime, #page, #size, #asc}")
    public ResponseEntity<PagedDataDto<ScheduleDto>> getSchedulesByDateTimeRange(
            @RequestParam("startDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam("endDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean asc) {
        return new ResponseEntity<>(scheduleService.getByDateTimeRange(startDateTime, endDateTime, page, size, asc), HttpStatus.OK);
    }

    @GetMapping("/status")
    @Cacheable(value = "schedules", key = "{#status, #page, #size, #asc}")
    public ResponseEntity<PagedDataDto<ScheduleDto>> getSchedulesByStatus(
            @RequestParam String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean asc) {
        return new ResponseEntity<>(scheduleService.getStatusList(status, page, size, asc), HttpStatus.OK);
    }
}
