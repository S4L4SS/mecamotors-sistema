package com.mecamotors.api.dtos;

import lombok.Data;

@Data
public class DetalleCotizacionRequest {
    private Long repuestoId;
    private Integer cantidad;
    private String descripcion;
}
