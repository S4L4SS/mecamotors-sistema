package com.mecamotors.api.controllers;

import com.mecamotors.api.dtos.VehiculoDTO;
import com.mecamotors.api.entities.models.Cliente;
import com.mecamotors.api.entities.models.Vehiculo;
import com.mecamotors.api.mappers.VehiculoMapper;
import com.mecamotors.api.repositories.ClienteRepository;
import com.mecamotors.api.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VehiculoController {

    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;
    private final VehiculoMapper vehiculoMapper;

    @GetMapping
    public List<VehiculoDTO> listarVehiculos() {
        return vehiculoMapper.toDtoList(vehiculoRepository.findAll());
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorPlaca(@RequestParam("placa") String placa) {
        Optional<Vehiculo> vehiculoOpt = vehiculoRepository.findByPlaca(placa);
        return vehiculoOpt
                .map(vehiculo -> ResponseEntity.ok(vehiculoMapper.toDto(vehiculo)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/por-cliente/{clienteId}")
    public List<VehiculoDTO> listarPorCliente(@PathVariable Long clienteId) {
        return vehiculoMapper.toDtoList(vehiculoRepository.findByClienteId(clienteId));
    }

    @PostMapping
    public ResponseEntity<?> crearVehiculo(@RequestBody VehiculoDTO dto) {
        Vehiculo entity = vehiculoMapper.toEntity(dto);
        if (dto.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
            entity.setCliente(cliente);
        }
        Vehiculo guardado = vehiculoRepository.save(entity);
        return ResponseEntity.ok(vehiculoMapper.toDto(guardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVehiculo(@PathVariable Long id, @RequestBody VehiculoDTO dto) {
        return vehiculoRepository.findById(id)
                .map(existente -> {
                    vehiculoMapper.updateEntityFromDto(dto, existente);
                    if (dto.getClienteId() != null) {
                        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
                        existente.setCliente(cliente);
                    }
                    Vehiculo guardado = vehiculoRepository.save(existente);
                    return ResponseEntity.ok(vehiculoMapper.toDto(guardado));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
