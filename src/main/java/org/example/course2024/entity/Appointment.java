package org.example.course2024.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.course2024.enums.StatusAppoint;

@Entity
@Table (name = "appointments")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "date_id", nullable = false)
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "master_id", nullable = false)
    private Master master;

    @Enumerated(EnumType.STRING)
    private StatusAppoint status;
}
