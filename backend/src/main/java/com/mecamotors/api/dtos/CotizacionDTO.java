package com.mecamotors.api.dtos;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CotizacionDTO {
    private Long id;
    private Long clienteId;
    private String clienteNombre;
    private Long vehiculoId;
    private String vehiculoPlaca;
    private String vehiculoMarca;
    private String vehiculoModelo;
    private LocalDateTime fecha;
    private String descripcion;
    private Double total;
    private String estado;
    private List<DetalleCotizacionDTO> detalles;
}
