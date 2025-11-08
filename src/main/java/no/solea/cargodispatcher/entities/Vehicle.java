package no.solea.cargodispatcher.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "vehicle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double speed; // e.g., distance per hour

    @Column(nullable = false)
    private Double capacity; // Maximum volume it can carry
}
