package org.example.course2024.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "masters")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Master extends Person {

    @Column(name = "done_works", nullable = false)
    private int doneWork;

    @OneToMany(mappedBy = "master", cascade = CascadeType.ALL)
    private List<Appointment> appointment;

    @OneToMany(mappedBy = "master", cascade = CascadeType.ALL)
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "master", cascade = CascadeType.ALL)
    private List<Price> price;

}
