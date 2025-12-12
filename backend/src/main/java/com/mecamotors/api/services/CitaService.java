package com.mecamotors.api.services;

import com.mecamotors.api.dtos.CitaDTO;
import com.mecamotors.api.entities.enums.EstadoCita;
import com.mecamotors.api.entities.models.Cita;
import com.mecamotors.api.entities.models.Cliente;
import com.mecamotors.api.entities.models.Vehiculo;
import com.mecamotors.api.repositories.CitaRepository;
import com.mecamotors.api.repositories.ClienteRepository;
import com.mecamotors.api.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final ClienteRepository clienteRepository;
    private final VehiculoRepository vehiculoRepository;

    @Transactional
    public Cita crearCita(CitaDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Vehiculo vehiculo = null;
        if (dto.getVehiculoId() != null) {
            vehiculo = vehiculoRepository.findById(dto.getVehiculoId())
                    .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        }

        Cita cita = Cita.builder()
                .fechaHora(dto.getFechaHora())
                .motivo(dto.getMotivo())
                .estado(EstadoCita.PENDIENTE)
                .cliente(cliente)
                .vehiculo(vehiculo)
                .build();

        return citaRepository.save(cita);
    }

    @Transactional(readOnly = true)
    public List<Cita> listarTodas() {
        return citaRepository.findAll();
    }

    @Transactional
    public Cita actualizarEstado(Long id, EstadoCita nuevoEstado) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        cita.setEstado(nuevoEstado);
        return citaRepository.save(cita);
    }
}