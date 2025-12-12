package com.mecamotors.api.services;

import com.mecamotors.api.dtos.VehiculoDTO;
import com.mecamotors.api.entities.models.Cliente;
import com.mecamotors.api.entities.models.Vehiculo;
import com.mecamotors.api.mappers.VehiculoMapper;
import com.mecamotors.api.repositories.ClienteRepository;
import com.mecamotors.api.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;
    private final VehiculoMapper vehiculoMapper;

    @Transactional(readOnly = true)
    public VehiculoDTO buscarPorPlaca(String placa) {
        return vehiculoRepository.findByPlaca(placa)
                .map(vehiculoMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Vehículo con placa " + placa + " no encontrado"));
    }

    @Transactional(readOnly = true)
    public List<VehiculoDTO> listarPorCliente(Long clienteId) {
        return vehiculoMapper.toDtoList(vehiculoRepository.findByClienteId(clienteId));
    }

    @Transactional
    public VehiculoDTO registrarVehiculo(VehiculoDTO dto) {
        // 1. Verificar si la placa ya existe
        if (vehiculoRepository.findByPlaca(dto.getPlaca()).isPresent()) {
            throw new RuntimeException("Ya existe un vehículo registrado con la placa: " + dto.getPlaca());
        }

        // 2. Validar dueño
        Cliente dueno = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente ID no encontrado"));

        // 3. Guardar
        Vehiculo vehiculo = vehiculoMapper.toEntity(dto);
        vehiculo.setCliente(dueno); // Asignación manual segura

        return vehiculoMapper.toDto(vehiculoRepository.save(vehiculo));
    }
}