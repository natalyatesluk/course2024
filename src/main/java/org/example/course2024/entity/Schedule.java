package org.example.course2024.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.course2024.enums.StatusTime;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table (name = "shedules")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private StatusTime status;

    @ManyToOne
    @JoinColumn(name = "master_id", nullable = false)
    private Master master;

}
