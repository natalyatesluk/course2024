package org.example.course2024.controller;


import org.example.course2024.dto.ScheduleCreationDto;
import org.example.course2024.dto.ScheduleDto;
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
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping()
    public ResponseEntity<List<ScheduleDto>> getSchedules() {
        return new ResponseEntity<>(scheduleService.getAll(), HttpStatus.OK);
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
    public ResponseEntity<ScheduleDto> updateSchedule(@PathVariable Long id, @RequestBody ScheduleCreationDto scheduleDto) {
        return new ResponseEntity<>(scheduleService.update(id, scheduleDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ScheduleDto> deleteSchedule(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
//    @GetMapping("/date-range")
//    public ResponseEntity<List<ScheduleDto>> getSchedulesByDateRange(
//            @RequestParam("startDate") LocalDate startDate,
//            @RequestParam("endDate") LocalDate endDate) {
//        List<ScheduleDto> schedules = scheduleService.getByDateRange(startDate, endDate);
//        return new ResponseEntity<>(schedules, HttpStatus.OK);
//    }
//
//    @GetMapping("/time-range")
//    public ResponseEntity<List<ScheduleDto>> getSchedulesByTimeRange(
//            @RequestParam("startTime") LocalTime startTime,
//            @RequestParam("endTime") LocalTime endTime) {
//        List<ScheduleDto> schedules = scheduleService.getByTimeRange(startTime, endTime);
//        return new ResponseEntity<>(schedules, HttpStatus.OK);
//    }
//
//    @GetMapping("/date-time-range")
//    public ResponseEntity<List<ScheduleDto>> getSchedulesByDateTimeRange(
//            @RequestParam("startDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
//            @RequestParam("endDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
//        List<ScheduleDto> schedules = scheduleService.getByDateTimeRange(startDateTime, endDateTime);
//        return new ResponseEntity<>(schedules, HttpStatus.OK);
//    }

    @GetMapping("/status")
    public ResponseEntity<List<ScheduleDto>> getSchedulesByStatus(@RequestParam String status) {
            List<ScheduleDto> freeSchedules = scheduleService.getStatusList(status);
            return new ResponseEntity<>(freeSchedules, HttpStatus.OK);
    }


}
