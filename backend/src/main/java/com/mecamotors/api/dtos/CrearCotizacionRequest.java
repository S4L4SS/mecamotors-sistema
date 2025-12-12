package com.mecamotors.api.dtos;

import lombok.Data;
import java.util.List;

@Data
public class CrearCotizacionRequest {
    private Long clienteId;
    private Long vehiculoId;
    private String descripcion;
    private List<DetalleCotizacionRequest> detalles;
}
