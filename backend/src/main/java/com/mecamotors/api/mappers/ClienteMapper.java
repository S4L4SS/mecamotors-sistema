package com.mecamotors.api.mappers;

import com.mecamotors.api.dtos.ClienteDTO;
import com.mecamotors.api.entities.models.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClienteMapper {
    ClienteDTO toDto(Cliente cliente);
    Cliente toEntity(ClienteDTO dto);
}