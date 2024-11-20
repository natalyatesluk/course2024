package org.example.course2024.controller;


import lombok.AllArgsConstructor;
import org.example.course2024.dto.PagedDataDto;
import org.example.course2024.dto.ScheduleCreationDto;
import org.example.course2024.dto.ScheduleDto;
import org.example.course2024.dto.ScheduleUpdatingDto;
import org.example.course2024.entity.Schedule;
import org.example.course2024.service.ScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@AllArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping()
    public ResponseEntity<PagedDataDto<ScheduleDto>> getSchedules(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean asc) {
        return new ResponseEntity<>(scheduleService.getAll(page, size, asc), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDto> getSchedule(@PathVariable Long id) {
        return new ResponseEntity<>(scheduleService.getById(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ScheduleDto> createSchedule(@RequestBody ScheduleCreationDto scheduleDto) {
        return new ResponseEntity<>(scheduleService.create(scheduleDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDto> updateSchedule(@PathVariable Long id, @RequestBody ScheduleUpdatingDto scheduleDto) {
        return new ResponseEntity<>(scheduleService.update(id, scheduleDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/date-range")
    public ResponseEntity<PagedDataDto<ScheduleDto>> getSchedulesByDateRange(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean asc) {
        return new ResponseEntity<>(scheduleService.getByDateRange(startDate, endDate, page, size, asc), HttpStatus.OK);
    }

    @GetMapping("/timeDateRange")
    public ResponseEntity<PagedDataDto<ScheduleDto>> getSchedulesByDateTimeRange(
            @RequestParam("startDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
            @RequestParam("endDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean asc) {
        return new ResponseEntity<>(scheduleService.getByDateTimeRange(startDateTime, endDateTime, page, size, asc), HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<PagedDataDto<ScheduleDto>> getSchedulesByStatus(
            @RequestParam String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean asc) {
        return new ResponseEntity<>(scheduleService.getStatusList(status, page, size, asc), HttpStatus.OK);
    }
}

