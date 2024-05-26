package dev.patika.VeterinaryManagementSystem.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "animal")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"customer", "appointmentDates", "vaccines"})
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column (name = "name", nullable = false)
    private String name;

    @Column (name = "species", nullable = false)
    private String species;

    @Column (name = "breed", nullable = false)
    private String breed;

    @Column (name = "gender", nullable = false)
    private String gender;

    @Column (name = "colour", nullable = false)
    private String colour;

    @Column (name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany (mappedBy = "animal", fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE)
    private List<AppointmentDate> appointmentDates;

    @OneToMany (mappedBy = "animal", fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE)
    private List <Vaccine> vaccines;

}