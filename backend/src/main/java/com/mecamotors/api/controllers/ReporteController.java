package com.mecamotors.api.controllers;

import com.mecamotors.api.dtos.OrdenTrabajoDTO;
import com.mecamotors.api.dtos.RepuestoDTO;
import com.mecamotors.api.entities.enums.EstadoOrden;
import com.mecamotors.api.entities.models.Repuesto;
import com.mecamotors.api.entities.models.OrdenTrabajo;
import com.mecamotors.api.mappers.OrdenTrabajoMapper;
import com.mecamotors.api.repositories.OrdenTrabajoRepository;
import com.mecamotors.api.repositories.RepuestoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReporteController {

    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OrdenTrabajoMapper ordenTrabajoMapper;
    private final RepuestoRepository repuestoRepository;

    // Reporte: repuestos faltantes (stock <= stockMinimo)
    @GetMapping("/repuestos-faltantes")
    public List<RepuestoDTO> repuestosFaltantes(@RequestParam(name = "stockMinimo", defaultValue = "5") Integer stockMinimo) {
        return repuestoRepository.findByStockActualLessThanEqual(stockMinimo).stream()
                .map(this::toRepuestoDto)
                .collect(Collectors.toList());
    }

    // Reporte: servicios realizados (ordenes TERMINADO o ENTREGADO)
    @GetMapping("/servicios-realizados")
    public List<OrdenTrabajoDTO> serviciosRealizados() {
        List<OrdenTrabajo> terminados = ordenTrabajoRepository.findByEstado(EstadoOrden.TERMINADO);
        List<OrdenTrabajo> entregados = ordenTrabajoRepository.findByEstado(EstadoOrden.ENTREGADO);
        terminados.addAll(entregados);
        return ordenTrabajoMapper.toDtoList(terminados);
    }

    private RepuestoDTO toRepuestoDto(Repuesto r) {
        RepuestoDTO dto = new RepuestoDTO();
        dto.setId(r.getId());
        dto.setNombre(r.getNombre());
        dto.setSku(r.getSku());
        dto.setPrecioVenta(r.getPrecioVenta());
        dto.setStockActual(r.getStockActual());
        return dto;
    }
}
