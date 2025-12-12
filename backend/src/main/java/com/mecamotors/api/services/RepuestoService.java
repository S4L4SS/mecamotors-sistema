package com.mecamotors.api.services;

import com.mecamotors.api.dtos.RepuestoDTO;
import com.mecamotors.api.entities.models.Repuesto;
import com.mecamotors.api.repositories.RepuestoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepuestoService {

    private final RepuestoRepository repuestoRepository;

    @Transactional(readOnly = true)
    public List<Repuesto> listarTodos() {
        return repuestoRepository.findAll();
    }

    @Transactional
    public Repuesto crearRepuesto(RepuestoDTO dto) {
        Repuesto repuesto = Repuesto.builder()
                .nombre(dto.getNombre())
                .sku(dto.getSku())
                .precioVenta(dto.getPrecioVenta())
                .stockActual(dto.getStockActual())
                .stockMinimo(5) // Default
                .build();
        return repuestoRepository.save(repuesto);
    }

    // Lógica vital: Validar y descontar stock
    @Transactional
    public void consumirStock(Long idRepuesto, Integer cantidad) {
        Repuesto repuesto = repuestoRepository.findById(idRepuesto)
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));

        if (repuesto.getStockActual() < cantidad) {
            throw new RuntimeException("Stock insuficiente para: " + repuesto.getNombre());
        }

        repuesto.setStockActual(repuesto.getStockActual() - cantidad);
        repuestoRepository.save(repuesto);
    }
}