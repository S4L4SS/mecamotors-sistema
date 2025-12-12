package com.mecamotors.api.dtos;

import lombok.Data;

@Data
public class DetalleCotizacionDTO {
    private Long id;
    private Long repuestoId;
    private String repuestoNombre;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
    private String descripcion;
}
