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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/schedule")
@AllArgsConstructor
@Tag(name = "Schedule API", description = "Operations for managing Schedule entries")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "Get all Schedules", description = "Retrieve a paginated list of all Schedules")
    @GetMapping()
    @Cacheable(value = "schedules", key = "{#page, #size, #asc}")
    public ResponseEntity<PagedDataDto<ScheduleDto>> getSchedules(
            @Parameter(description = "Page number for pagination") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size for pagination") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort order: true for ascending, false for descending") @RequestParam(defaultValue = "true") boolean asc) {
        return new ResponseEntity<>(scheduleService.getAll(page, size, asc), HttpStatus.OK);
    }

    @Operation(summary = "Get Schedule by ID", description = "Retrieve a single Schedule by its unique ID")
    @GetMapping("/{id}")
    @Cacheable(value = "scheduleById", key = "#id")
    public ResponseEntity<ScheduleDto> getSchedule(
            @Parameter(description = "ID of the Schedule to retrieve") @PathVariable Long id) {
        return new ResponseEntity<>(scheduleService.getById(id), HttpStatus.OK);
    }

    @Operation(summary = "Create a new Schedule", description = "Add a new Schedule to the system")
    @PostMapping()
    @CacheEvict(value = {"schedules"}, allEntries = true)
    public ResponseEntity<ScheduleDto> createSchedule(@Valid @RequestBody ScheduleCreationDto scheduleDto) {
        return new ResponseEntity<>(scheduleService.create(scheduleDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update Schedule by ID", description = "Update details of an existing Schedule by its ID")
    @PutMapping("/{id}")
    @CacheEvict(value = {"schedules", "scheduleById"}, allEntries = true)
    public ResponseEntity<ScheduleDto> updateSchedule(
            @Parameter(description = "ID of the Schedule to update") @PathVariable Long id,
            @Valid @RequestBody ScheduleUpdatingDto scheduleDto) {
        return new ResponseEntity<>(scheduleService.update(id, scheduleDto), HttpStatus.OK);
    }

    @Operation(summary = "Delete Schedule by ID", description = "Delete an existing Schedule by its unique ID")
    @DeleteMapping("/{id}")
    @CacheEvict(value = {"schedules", "scheduleById"}, allEntries = true)
    public ResponseEntity<Void> deleteSchedule(
            @Parameter(description = "ID of the Schedule to delete") @PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get Schedules by Date Range", description = "Retrieve Schedules within a specified date range")
    @GetMapping("/date-range")
    @Cacheable(value = "schedules", key = "{#startDate, #endDate, #page, #size, #asc}")
    public ResponseEntity<PagedDataDto<ScheduleDto>> getSchedulesByDateRange(
            @Parameter(description = "Start date for the range") @RequestParam("startDate") LocalDate startDate,
            @Parameter(description = "End date for the range") @RequestParam("endDate") LocalDate endDate,
            @Parameter(description = "Page number for pagination") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size for pagination") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort order: true for ascending, false for descending") @RequestParam(defaultValue = "true") boolean asc) {
        return new ResponseEntity<>(scheduleService.getByDateRange(startDate, endDate, page, size, asc), HttpStatus.OK);
    }

    @Operation(summary = "Get Schedules by DateTime Range", description = "Retrieve Schedules within a specified date-time range")
    @GetMapping("/timeDateRange")
    @Cacheable(value = "schedules", key = "{#startDateTime, #endDateTime, #page, #size, #asc}")
    public ResponseEntity<PagedDataDto<ScheduleDto>> getSchedulesByDateTimeRange(
            @Parameter(description = "Start date-time for the range") @RequestParam("startDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
            @Parameter(description = "End date-time for the range") @RequestParam("endDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime,
            @Parameter(description = "Page number for pagination") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size for pagination") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort order: true for ascending, false for descending") @RequestParam(defaultValue = "true") boolean asc) {
        return new ResponseEntity<>(scheduleService.getByDateTimeRange(startDateTime, endDateTime, page, size, asc), HttpStatus.OK);
    }

    @Operation(summary = "Get Schedules by Status", description = "Retrieve Schedules filtered by status")
    @GetMapping("/status")
    @Cacheable(value = "schedules", key = "{#status, #page, #size, #asc}")
    public ResponseEntity<PagedDataDto<ScheduleDto>> getSchedulesByStatus(
            @Parameter(description = "Status of the Schedule") @RequestParam String status,
            @Parameter(description = "Page number for pagination") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size for pagination") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort order: true for ascending, false for descending") @RequestParam(defaultValue = "true") boolean asc) {
        return new ResponseEntity<>(scheduleService.getStatusList(status, page, size, asc), HttpStatus.OK);
    }
}
