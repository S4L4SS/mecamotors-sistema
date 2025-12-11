package com.mecamotors.api.controllers;

import com.mecamotors.api.entities.enums.EstadoCita;
import com.mecamotors.api.entities.models.Cita;
import com.mecamotors.api.entities.models.Cliente;
import com.mecamotors.api.entities.models.Vehiculo;
import com.mecamotors.api.repositories.CitaRepository;
import com.mecamotors.api.repositories.ClienteRepository;
import com.mecamotors.api.repositories.VehiculoRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CitaController {

    private final CitaRepository citaRepository;
    private final ClienteRepository clienteRepository;
    private final VehiculoRepository vehiculoRepository;

    @PostMapping
    public ResponseEntity<?> crearCitaDesdeWeb(@RequestBody CrearCitaRequest request) {
        // 1. Buscar o crear cliente por teléfono
        Cliente cliente = clienteRepository.findByTelefono(request.getTelefono())
                .orElseGet(() -> {
                    String nombreCompleto = request.getNombre() == null ? "" : request.getNombre().trim();
                    String nombre = nombreCompleto;
                    String apellido = "";
                    int idx = nombreCompleto.indexOf(' ');
                    if (idx > 0) {
                        nombre = nombreCompleto.substring(0, idx);
                        apellido = nombreCompleto.substring(idx + 1);
                    }
                    Cliente nuevo = Cliente.builder()
                            .nombre(nombre)
                            .apellido(apellido)
                            .telefono(request.getTelefono())
                            .email(null)
                            .build();
                    return clienteRepository.save(nuevo);
                });

        // 2. Buscar o crear vehículo por placa
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(request.getPlaca())
                .orElseGet(() -> {
                    Vehiculo nuevo = Vehiculo.builder()
                            .placa(request.getPlaca())
                            .marca(null)
                            .modelo(null)
                            .anio(null)
                            .color(null)
                            .cliente(cliente)
                            .build();
                    return vehiculoRepository.save(nuevo);
                });

        // 3. Construir la fecha/hora de la cita
        LocalDateTime fechaHora = LocalDateTime.now();
        if (request.getFecha() != null && !request.getFecha().isBlank()) {
            try {
                LocalDate fecha = LocalDate.parse(request.getFecha());
                fechaHora = LocalDateTime.of(fecha, LocalTime.of(10, 0)); // 10:00 am por defecto
            } catch (Exception ignored) {
            }
        }

        // 4. Crear y guardar la cita
        String motivo = request.getServicio() != null
                ? request.getServicio() + " - cita web"
                : "Cita web";

        Cita cita = Cita.builder()
                .cliente(cliente)
                .vehiculo(vehiculo)
                .fechaHora(fechaHora)
                .motivo(motivo)
                .estado(EstadoCita.PENDIENTE)
                .build();

        citaRepository.save(cita);

        return ResponseEntity.ok("Cita registrada correctamente");
    }

    @Data
    public static class CrearCitaRequest {
        private String nombre;
        private String telefono;
        private String placa;
        private String servicio;
        private String fecha; // formato yyyy-MM-dd enviado desde el frontend
    }
}
