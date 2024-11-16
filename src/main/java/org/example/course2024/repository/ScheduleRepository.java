package org.example.course2024.repository;

import org.example.course2024.entity.Master;
import org.example.course2024.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query
    Schedule findByDateAndMaster(LocalDateTime date, Master master);
}
