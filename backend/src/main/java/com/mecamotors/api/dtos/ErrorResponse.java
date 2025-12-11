package com.mecamotors.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int estado;        // Ej: 400, 404
    private String mensaje;    // Ej: "El usuario ya existe"
    private LocalDateTime fecha;
}