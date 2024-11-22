package org.example.course2024.repository;

import org.example.course2024.entity.Master;
import org.example.course2024.entity.Price;
import org.example.course2024.entity.Schedule;
import org.example.course2024.enums.StatusTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query
    Schedule findByDateAndMaster(LocalDateTime date, Master master);

    @Query("SELECT s FROM Schedule s WHERE FUNCTION('DATE', s.date) BETWEEN :startDate AND :endDate")
    List<Schedule> findByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    @Query("SELECT s FROM Schedule s WHERE FUNCTION('DATE', s.date) BETWEEN :startDate AND :endDate")
    Page<Schedule> findByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    @Query(value = "SELECT * FROM schedule s WHERE CAST(s.date AS TIME) BETWEEN :startTime AND :endTime", nativeQuery = true)
    List<Schedule> findByTimeRange(
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);


    @Query("SELECT s FROM Schedule s WHERE s.date BETWEEN :startDateTime AND :endDateTime")
    Page<Schedule> findByDateTimeRange(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            Pageable pageable);

    @Query("SELECT s FROM Schedule s WHERE s.status = :status")
    Page<Schedule> findByStatus(@Param("status") StatusTime status, Pageable pageable);

    @Query("SELECT s FROM Schedule s WHERE s.date BETWEEN :startDateTime AND :endDateTime")
    List<Schedule> findByDateTimeRange(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);

    @Query("SELECT s FROM Schedule s WHERE s.status = :status")
    List<Schedule> findByStatus(@Param("status") StatusTime status);

    @Query("SELECT p FROM Schedule p WHERE p.master.id = :idMaster")
    List<Schedule> findByMaster(Long idMaster);
}
