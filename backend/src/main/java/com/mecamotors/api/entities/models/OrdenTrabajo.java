package com.mecamotors.api.entities.models;

import com.mecamotors.api.entities.enums.EstadoOrden;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ordenes_trabajo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenTrabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private LocalDateTime fechaIngreso;

    @PrePersist
    protected void onCreate() {
        fechaIngreso = LocalDateTime.now();
    }

    @Enumerated(EnumType.STRING)
    private EstadoOrden estado;

    @Column(columnDefinition = "TEXT")
    private String diagnostico;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "mecanico_id")
    private Usuario mecanico; // El mecánico asignado

    @OneToMany(mappedBy = "ordenTrabajo", cascade = CascadeType.ALL)
    private List<DetalleOrden> detalles;
}