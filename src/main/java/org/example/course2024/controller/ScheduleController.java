package org.example.course2024.controller;


import org.example.course2024.dto.ScheduleCreationDto;
import org.example.course2024.dto.ScheduleDto;
import org.example.course2024.entity.Schedule;
import org.example.course2024.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ScheduleDto> updateSchedule(@PathVariable Long id, @RequestBody ScheduleDto scheduleDto) {
        return new ResponseEntity<>(scheduleService.update(id, scheduleDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ScheduleDto> deleteSchedule(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
