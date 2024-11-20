package org.example.course2024.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Setter
@Getter
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "middleName", nullable = false)
    private String middleName;

    @Column(name = "phone", nullable = false,unique = true)
    private String phone;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

}
