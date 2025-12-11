package com.mecamotors.api.entity.models;

import com.mecamotors.api.entity.enums.TipoDetalle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "detalles_orden")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleOrden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orden_id", nullable = false)
    private OrdenTrabajo ordenTrabajo;

    @Enumerated(EnumType.STRING)
    private TipoDetalle tipo; // REPUESTO o SERVICIO

    @ManyToOne
    @JoinColumn(name = "repuesto_id")
    private Repuesto repuesto; // Puede ser null si es mano de obra

    private String descripcionServicio; // Si es servicio, se llena esto
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}