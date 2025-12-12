package com.mecamotors.api.services;

import com.mecamotors.api.dtos.*;
import com.mecamotors.api.entities.enums.EstadoOrden;
import com.mecamotors.api.entities.enums.TipoDetalle;
import com.mecamotors.api.mappers.OrdenTrabajoMapper;
import com.mecamotors.api.entities.models.*;
import com.mecamotors.api.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdenTrabajoService {

    private final OrdenTrabajoRepository ordenRepository;
    private final VehiculoRepository vehiculoRepository;
    private final UsuarioRepository usuarioRepository; // Para buscar mecánico
    private final RepuestoRepository repuestoRepository;
    private final OrdenTrabajoMapper ordenMapper;

    // 1. Crear Orden (Cuando el auto llega al taller)
    @Transactional
    public OrdenTrabajoDTO crearOrden(CrearOrdenRequest request) {
        Vehiculo vehiculo = vehiculoRepository.findById(request.getVehiculoId())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        Usuario mecanico = null;
        if (request.getMecanicoId() != null) {
            mecanico = usuarioRepository.findById(request.getMecanicoId())
                    .orElseThrow(() -> new RuntimeException("Mecánico no encontrado"));
        }

        OrdenTrabajo orden = OrdenTrabajo.builder()
                .vehiculo(vehiculo)
                .mecanico(mecanico)
                .estado(EstadoOrden.DIAGNOSTICO) // Estado inicial
                .diagnostico(request.getDiagnosticoInicial())
                .totalEstimado(BigDecimal.ZERO)
                .build();

        OrdenTrabajo guardada = ordenRepository.save(orden);
        return ordenMapper.toDto(guardada);
    }

    // 2. Actualizar Diagnóstico y Estado
    @Transactional
    public OrdenTrabajoDTO actualizarEstado(Long idOrden, EstadoOrden nuevoEstado, String diagnostico) {
        OrdenTrabajo orden = ordenRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        if (nuevoEstado != null) orden.setEstado(nuevoEstado);
        if (diagnostico != null) orden.setDiagnostico(diagnostico);

        return ordenMapper.toDto(ordenRepository.save(orden));
    }

    // 3. Agregar Items (Repuestos o Servicios) a la Orden
    @Transactional
    public OrdenTrabajoDTO agregarDetalle(Long idOrden, DetalleOrdenDTO detalleDto) {
        OrdenTrabajo orden = ordenRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        DetalleOrden detalle = new DetalleOrden();
        detalle.setOrdenTrabajo(orden);
        detalle.setCantidad(detalleDto.getCantidad());

        // Logica para diferenciar Repuesto vs Mano de Obra
        if ("REPUESTO".equalsIgnoreCase(detalleDto.getTipo())) {
            detalle.setTipo(TipoDetalle.REPUESTO);

            Repuesto repuesto = repuestoRepository.findById(detalleDto.getRepuestoId())
                    .orElseThrow(() -> new RuntimeException("Repuesto no existe"));

            // Validamos stock
            if (repuesto.getStockActual() < detalleDto.getCantidad()) {
                throw new RuntimeException("No hay suficiente stock de: " + repuesto.getNombre());
            }

            detalle.setRepuesto(repuesto);
            detalle.setPrecioUnitario(repuesto.getPrecioVenta());
            detalle.setDescripcionServicio(repuesto.getNombre()); // Opcional, por redundancia

        } else {
            // Es MANO DE OBRA
            detalle.setTipo(TipoDetalle.SERVICIO);
            detalle.setDescripcionServicio(detalleDto.getDescripcion());
            detalle.setPrecioUnitario(detalleDto.getPrecioUnitario()); // El mecánico pone el precio
        }

        // Calcular Subtotal
        BigDecimal subtotal = detalle.getPrecioUnitario().multiply(new BigDecimal(detalle.getCantidad()));
        detalle.setSubtotal(subtotal);

        // Actualizar el Total de la Orden (Cabecera)
        BigDecimal nuevoTotal = orden.getTotalEstimado() == null ? BigDecimal.ZERO : orden.getTotalEstimado();
        orden.setTotalEstimado(nuevoTotal.add(subtotal));

        // Guardar detalle
        orden.getDetalles().add(detalle);
        ordenRepository.save(orden);

        return ordenMapper.toDto(orden);
    }

    // 4. Listar órdenes por estado
    public List<OrdenTrabajoDTO> listarPorEstado(EstadoOrden estado) {
        return ordenMapper.toDtoList(ordenRepository.findByEstado(estado));
    }
}