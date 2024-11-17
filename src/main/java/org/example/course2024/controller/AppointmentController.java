package org.example.course2024.controller;


import lombok.AllArgsConstructor;
import org.example.course2024.dto.AppointmentCreationDto;
import org.example.course2024.dto.AppointmentDto;
import org.example.course2024.entity.Appointment;
import org.example.course2024.enums.StatusAppoint;
import org.example.course2024.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointment")
@AllArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping()
    public ResponseEntity<List<AppointmentDto>> getAppointment() {
        return new ResponseEntity<>(appointmentService.getAll(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id) {
        return new ResponseEntity<>(appointmentService.getById(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentCreationDto appointmentDto) {
    return new ResponseEntity<>(appointmentService.create(appointmentDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDto> updateAppointment(@PathVariable Long id,@RequestBody AppointmentDto appointmentDto) {
        return new ResponseEntity<>(appointmentService.update(appointmentDto,id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppointmentDto> deleteAppointment(@PathVariable Long id) {
        appointmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/status-counts")
    public ResponseEntity<Map<StatusAppoint, Long>> getStatusCounts(@RequestParam(defaultValue = "true") boolean status) {
        if(status) {
            Map<StatusAppoint, Long> statusCounts = appointmentService.getStatusCounts();
            return new ResponseEntity<>(statusCounts, HttpStatus.OK);
        }
        return ResponseEntity.noContent().build();
    }

}
