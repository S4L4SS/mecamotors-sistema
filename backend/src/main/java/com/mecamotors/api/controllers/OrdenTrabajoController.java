package com.mecamotors.api.controllers;

import com.mecamotors.api.dtos.OrdenTrabajoDTO;
import com.mecamotors.api.entities.enums.EstadoOrden;
import com.mecamotors.api.entities.models.OrdenTrabajo;
import com.mecamotors.api.mappers.OrdenTrabajoMapper;
import com.mecamotors.api.repositories.OrdenTrabajoRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrdenTrabajoController {

    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OrdenTrabajoMapper ordenTrabajoMapper;

    private static final DateTimeFormatter FECHA_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault());

    @GetMapping
    public List<OrdenTrabajoDTO> listarOrdenes() {
        List<OrdenTrabajo> ordenes = ordenTrabajoRepository.findAll();
        return ordenTrabajoMapper.toDtoList(ordenes);
    }

    @GetMapping("/estado")
    public ResponseEntity<EstadoOrdenResponse> consultarEstado(
            @RequestParam(value = "placa", required = false) String placa,
            @RequestParam(value = "numeroOrden", required = false) Long numeroOrden
    ) {
        Optional<OrdenTrabajo> ordenOpt;

        if (numeroOrden != null) {
            ordenOpt = ordenTrabajoRepository.findById(numeroOrden);
        } else if (placa != null && !placa.isBlank()) {
            ordenOpt = ordenTrabajoRepository.findOrdenesActivasPorPlaca(placa)
                    .stream()
                    .findFirst();
        } else {
            return ResponseEntity.badRequest().build();
        }

        return ordenOpt
                .map(orden -> {
                    EstadoOrdenResponse resp = new EstadoOrdenResponse();
                    resp.setEstado(orden.getEstado() != null ? orden.getEstado().name() : "DESCONOCIDO");
                    resp.setDescripcion(orden.getDiagnostico());
                    if (orden.getFechaIngreso() != null) {
                        resp.setUltimaActualizacion(orden.getFechaIngreso().format(FECHA_FORMATTER));
                    }
                    return ResponseEntity.ok(resp);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/por-estado")
    public List<OrdenTrabajoDTO> listarPorEstado(@RequestParam("estado") EstadoOrden estado) {
        List<OrdenTrabajo> ordenes = ordenTrabajoRepository.findByEstado(estado);
        return ordenTrabajoMapper.toDtoList(ordenes);
    }

    @Data
    public static class EstadoOrdenResponse {
        private String estado;
        private String descripcion;
        private String ultimaActualizacion;
    }
}
