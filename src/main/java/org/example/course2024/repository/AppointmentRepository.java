package org.example.course2024.repository;

import org.example.course2024.entity.Appointment;
import org.example.course2024.entity.Price;
import org.example.course2024.entity.Schedule;
import org.example.course2024.enums.StatusAppoint;
import org.example.course2024.enums.StatusTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
    List<Object[]> countAppointmentByStatus();

    @Query("from Appointment ")
    public List<Appointment> findAllByPage(PageRequest pageRequest);

    @Query("SELECT a FROM Appointment a WHERE a.status = :status")
    Page<Appointment> findByStatus(@Param("status") StatusAppoint status, Pageable pageable);

    @Query("SELECT p FROM Appointment p WHERE p.master.id = :idMaster")
    List<Appointment> findByMaster(Long idMaster);
}
