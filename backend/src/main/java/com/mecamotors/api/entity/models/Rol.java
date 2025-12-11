package com.mecamotors.api.entity.models;

import jakarta.persistence.*;
import lombok.*;
import com.mecamotors.api.entity.enums.RolNombre;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RolNombre nombre;
}