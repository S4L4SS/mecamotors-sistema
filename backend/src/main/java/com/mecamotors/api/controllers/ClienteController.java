package com.mecamotors.api.controllers;

import com.mecamotors.api.dtos.ClienteDTO;
import com.mecamotors.api.entities.models.Cliente;
import com.mecamotors.api.mappers.ClienteMapper;
import com.mecamotors.api.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @GetMapping
    public List<ClienteDTO> listarClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> crearCliente(@RequestBody ClienteDTO dto) {
        Cliente entity = clienteMapper.toEntity(dto);
        entity.setId(null); // por si acaso viene id en el DTO
        Cliente guardado = clienteRepository.save(entity);
        return ResponseEntity.ok(clienteMapper.toDto(guardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizarCliente(@PathVariable Long id, @RequestBody ClienteDTO dto) {
        return clienteRepository.findById(id)
                .map(existente -> {
                    existente.setNombre(dto.getNombre());
                    existente.setApellido(dto.getApellido());
                    existente.setTelefono(dto.getTelefono());
                    existente.setEmail(dto.getEmail());
                    Cliente guardado = clienteRepository.save(existente);
                    return ResponseEntity.ok(clienteMapper.toDto(guardado));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public List<ClienteDTO> buscarClientes(@RequestParam("q") String q) {
        String filtro = q == null ? "" : q.toLowerCase(Locale.ROOT);
        return clienteRepository.findAll().stream()
                .filter(c ->
                        (c.getNombre() != null && c.getNombre().toLowerCase(Locale.ROOT).contains(filtro)) ||
                        (c.getApellido() != null && c.getApellido().toLowerCase(Locale.ROOT).contains(filtro)) ||
                        (c.getEmail() != null && c.getEmail().toLowerCase(Locale.ROOT).contains(filtro)) ||
                        (c.getTelefono() != null && c.getTelefono().toLowerCase(Locale.ROOT).contains(filtro))
                )
                .map(clienteMapper::toDto)
                .collect(Collectors.toList());
    }
}
