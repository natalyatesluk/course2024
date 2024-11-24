    package org.example.course2024.controller;


    import jakarta.validation.Valid;
    import lombok.AllArgsConstructor;
    import org.example.course2024.dto.*;
    import org.example.course2024.enums.StatusAppoint;
    import org.example.course2024.service.AppointmentService;
    import org.springframework.cache.annotation.CacheEvict;
    import org.springframework.cache.annotation.CachePut;
    import org.springframework.cache.annotation.Cacheable;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Sort;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.web.bind.annotation.*;

    import java.util.Map;

    @RestController
    @RequestMapping("/api/appointment")
    @AllArgsConstructor
    public class AppointmentController {
        private final AppointmentService appointmentService;

        @Cacheable(value = "appointment", key = "'page_' + #page + '_size_' + #size")
        @GetMapping()
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<PagedDataDto<AppointmentDto>> getAppointment(
                @RequestParam(required = false, defaultValue = "0") int page,
                @RequestParam(required = false, defaultValue = "5") int size
        ) {
            return new ResponseEntity<>(appointmentService.getAll(PageRequest.of(page,size)), HttpStatus.OK);
        }

        @Cacheable(value = "appointment", key = "#id")
        @GetMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id) {
            return new ResponseEntity<>(appointmentService.getById(id), HttpStatus.OK);
        }

        @CachePut(value = "appointment")
        @PostMapping()
        public ResponseEntity<AppointmentDto> createAppointment(@Valid  @RequestBody AppointmentCreationDto appointmentDto) {
            return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.create(appointmentDto));
        }

        @CachePut(value = "appointment", key = "'id_' + #id")
        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<AppointmentDto> updateAppointment(@Valid @PathVariable Long id,@RequestBody AppointmentUpdatingDto appointmentDto) {
            return new ResponseEntity<>(appointmentService.update(appointmentDto,id), HttpStatus.OK);
        }

        @CacheEvict(value = "appointment", key = "#id")
        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<AppointmentDto> deleteAppointment(@PathVariable Long id) {
            appointmentService.delete(id);
            return ResponseEntity.noContent().build();
        }

        @Cacheable(value = "appointment", key = "'status_' + #status")
        @GetMapping("/status-counts")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<Map<StatusAppoint, Long>> getStatusCounts(@RequestParam(defaultValue = "true") boolean status) {
            if(status) {
                Map<StatusAppoint, Long> statusCounts = appointmentService.getStatusCounts();
                return new ResponseEntity<>(statusCounts, HttpStatus.OK);
            }
            return ResponseEntity.noContent().build();
        }

        @Cacheable(value = "appointment", key = "'search_' + #keyword + '_page_' + #page + '_size_' + #size")
        @GetMapping("/search")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<PagedDataDto<AppointmentDto>> searchPrice(@RequestParam String keyword,
                                                                  @RequestParam(required = false, defaultValue = "0") int page,
                                                                  @RequestParam(required = false, defaultValue = "3") int size) {
            return new ResponseEntity<>(appointmentService.search(keyword, PageRequest.of(page, size)), HttpStatus.OK);
        }

        @Cacheable(value = "appointment", key = "'sort_' + #keyword + '_reverse_' + #reverse + '_page_' + #page + '_size_' + #size")
        @GetMapping("/sort")
        public ResponseEntity<PagedDataDto<AppointmentDto>> sortPrice(@RequestParam String keyword,
                                                                @RequestParam(defaultValue = "false") boolean reverse,
                                                                @RequestParam(required = false, defaultValue = "0") int page,
                                                                @RequestParam(required = false, defaultValue = "6") int size) {

            Sort sort = Sort.by(reverse ? Sort.Direction.DESC : Sort.Direction.ASC, keyword);
            return new ResponseEntity<>(appointmentService.getAll(PageRequest.of(page, size, sort)), HttpStatus.OK);
        }

        @GetMapping("/status")
        @Cacheable(value = "appointment", key = "{#status, #page, #size, #asc}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<PagedDataDto<AppointmentDto>> getSchedulesByStatus(
                @RequestParam String status,
                @RequestParam(required = false, defaultValue = "0") int page,
                @RequestParam(required = false,defaultValue = "10") int size,
                @RequestParam(required = false, defaultValue = "true") boolean asc) {
            return new ResponseEntity<>(appointmentService.getStatusList(status, page, size, asc), HttpStatus.OK);
        }


    }
