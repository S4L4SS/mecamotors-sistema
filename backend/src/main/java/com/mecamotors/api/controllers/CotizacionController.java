package com.mecamotors.api.controllers;

import com.mecamotors.api.dtos.*;
import com.mecamotors.api.entities.models.*;
import com.mecamotors.api.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cotizaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CotizacionController {

    private final CotizacionRepository cotizacionRepository;
    private final ClienteRepository clienteRepository;
    private final VehiculoRepository vehiculoRepository;
    private final RepuestoRepository repuestoRepository;

    @GetMapping
    public ResponseEntity<List<CotizacionDTO>> listarTodas() {
        List<Cotizacion> cotizaciones = cotizacionRepository.findAllByOrderByFechaDesc();
        return ResponseEntity.ok(cotizaciones.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CotizacionDTO>> listarPorCliente(@PathVariable Long clienteId) {
        List<Cotizacion> cotizaciones = cotizacionRepository.findByClienteId(clienteId);
        return ResponseEntity.ok(cotizaciones.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CotizacionDTO> obtenerPorId(@PathVariable Long id) {
        Cotizacion cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));
        return ResponseEntity.ok(convertirADTO(cotizacion));
    }

    @PostMapping
    public ResponseEntity<CotizacionDTO> crear(@RequestBody CrearCotizacionRequest request) {
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        Vehiculo vehiculo = vehiculoRepository.findById(request.getVehiculoId())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        Cotizacion cotizacion = Cotizacion.builder()
                .cliente(cliente)
                .vehiculo(vehiculo)
                .fecha(LocalDateTime.now())
                .descripcion(request.getDescripcion())
                .estado("PENDIENTE")
                .total(0.0)
                .build();

        double total = 0.0;

        for (DetalleCotizacionRequest detalleReq : request.getDetalles()) {
            Repuesto repuesto = repuestoRepository.findById(detalleReq.getRepuestoId())
                    .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));

            double precioUnitario = repuesto.getPrecioVenta() != null ? repuesto.getPrecioVenta().doubleValue() : 0.0;
            double subtotal = precioUnitario * detalleReq.getCantidad();
            total += subtotal;

            DetalleCotizacion detalle = DetalleCotizacion.builder()
                    .cotizacion(cotizacion)
                    .repuesto(repuesto)
                    .cantidad(detalleReq.getCantidad())
                    .precioUnitario(precioUnitario)
                    .subtotal(subtotal)
                    .descripcion(detalleReq.getDescripcion())
                    .build();

            cotizacion.getDetalles().add(detalle);
        }

        cotizacion.setTotal(total);
        cotizacionRepository.save(cotizacion);

        return ResponseEntity.ok(convertirADTO(cotizacion));
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GERENTE')")
    public ResponseEntity<CotizacionDTO> cambiarEstado(@PathVariable Long id, @RequestBody String nuevoEstado) {
        Cotizacion cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));
        
        cotizacion.setEstado(nuevoEstado.replace("\"", ""));
        cotizacionRepository.save(cotizacion);
        
        return ResponseEntity.ok(convertirADTO(cotizacion));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cotizacionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private CotizacionDTO convertirADTO(Cotizacion cotizacion) {
        CotizacionDTO dto = new CotizacionDTO();
        dto.setId(cotizacion.getId());
        dto.setClienteId(cotizacion.getCliente().getId());
        dto.setClienteNombre(cotizacion.getCliente().getNombre() + " " + cotizacion.getCliente().getApellido());
        dto.setVehiculoId(cotizacion.getVehiculo().getId());
        dto.setVehiculoPlaca(cotizacion.getVehiculo().getPlaca());
        dto.setVehiculoMarca(cotizacion.getVehiculo().getMarca());
        dto.setVehiculoModelo(cotizacion.getVehiculo().getModelo());
        dto.setFecha(cotizacion.getFecha());
        dto.setDescripcion(cotizacion.getDescripcion());
        dto.setTotal(cotizacion.getTotal());
        dto.setEstado(cotizacion.getEstado());

        List<DetalleCotizacionDTO> detallesDTO = cotizacion.getDetalles().stream()
                .map(this::convertirDetalleADTO)
                .collect(Collectors.toList());
        dto.setDetalles(detallesDTO);

        return dto;
    }

    private DetalleCotizacionDTO convertirDetalleADTO(DetalleCotizacion detalle) {
        DetalleCotizacionDTO dto = new DetalleCotizacionDTO();
        dto.setId(detalle.getId());
        dto.setRepuestoId(detalle.getRepuesto().getId());
        dto.setRepuestoNombre(detalle.getRepuesto().getNombre());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());
        dto.setDescripcion(detalle.getDescripcion());
        return dto;
    }
}
