package com.mecamotors.api.entities.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalle_cotizacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleCotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cotizacion_id")
    private Cotizacion cotizacion;

    @ManyToOne
    @JoinColumn(name = "repuesto_id")
    private Repuesto repuesto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precioUnitario;

    @Column(nullable = false)
    private Double subtotal;

    @Column(length = 500)
    private String descripcion;
}
