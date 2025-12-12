package com.mecamotors.api.entities.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cotizaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private Double total;

    @Column(length = 50)
    private String estado; // PENDIENTE, APROBADA, RECHAZADA

    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DetalleCotizacion> detalles = new ArrayList<>();
}
