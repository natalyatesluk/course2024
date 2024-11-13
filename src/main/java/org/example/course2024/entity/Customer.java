package org.example.course2024.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.course2024.enums.PartBody;

import java.util.List;

@Entity
@Table (name = "customers")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends Person {

    @Enumerated(EnumType.STRING)
    private PartBody partBody;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Appointment> appointments;
}
