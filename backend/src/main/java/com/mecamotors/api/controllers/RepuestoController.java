package com.mecamotors.api.controllers;

import com.mecamotors.api.dtos.RepuestoDTO;
import com.mecamotors.api.entities.models.Repuesto;
import com.mecamotors.api.repositories.RepuestoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/repuestos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RepuestoController {

    private final RepuestoRepository repuestoRepository;

    @GetMapping
    public List<RepuestoDTO> listar() {
        return repuestoRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<RepuestoDTO> crear(@RequestBody RepuestoDTO dto) {
        Repuesto entity = toEntity(dto);
        entity.setId(null);
        Repuesto guardado = repuestoRepository.save(entity);
        return ResponseEntity.ok(toDto(guardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepuestoDTO> actualizar(@PathVariable Long id, @RequestBody RepuestoDTO dto) {
        return repuestoRepository.findById(id)
                .map(existente -> {
                    existente.setNombre(dto.getNombre());
                    existente.setSku(dto.getSku());
                    existente.setPrecioVenta(dto.getPrecioVenta());
                    existente.setStockActual(dto.getStockActual());
                    Repuesto guardado = repuestoRepository.save(existente);
                    return ResponseEntity.ok(toDto(guardado));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/bajo-stock")
    public List<RepuestoDTO> listarBajoStock(@RequestParam(name = "stockMinimo", defaultValue = "5") Integer stockMinimo) {
        return repuestoRepository.findByStockActualLessThanEqual(stockMinimo).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/buscar")
    public List<RepuestoDTO> buscarPorNombre(@RequestParam("nombre") String nombre) {
        return repuestoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private RepuestoDTO toDto(Repuesto r) {
        RepuestoDTO dto = new RepuestoDTO();
        dto.setId(r.getId());
        dto.setNombre(r.getNombre());
        dto.setSku(r.getSku());
        dto.setPrecioVenta(r.getPrecioVenta());
        dto.setStockActual(r.getStockActual());
        return dto;
    }

    private Repuesto toEntity(RepuestoDTO dto) {
        Repuesto r = new Repuesto();
        r.setId(dto.getId());
        r.setNombre(dto.getNombre());
        r.setSku(dto.getSku());
        r.setPrecioVenta(dto.getPrecioVenta());
        r.setStockActual(dto.getStockActual());
        // stockMinimo y precioCosto los podrías manejar en otra pantalla
        return r;
    }
}
