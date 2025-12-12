package com.mecamotors.api.services;

import com.mecamotors.api.dtos.ClienteDTO;
import com.mecamotors.api.mappers.ClienteMapper;
import com.mecamotors.api.entities.models.Cliente;
import com.mecamotors.api.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Transactional(readOnly = true)
    public List<ClienteDTO> listarTodos() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream().map(clienteMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ClienteDTO buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .map(clienteMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    @Transactional
    public ClienteDTO guardarCliente(ClienteDTO dto) {
        Cliente cliente = clienteMapper.toEntity(dto);
        return clienteMapper.toDto(clienteRepository.save(cliente));
    }
}