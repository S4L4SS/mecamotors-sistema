package com.mecamotors.api.mappers;

import com.mecamotors.api.dtos.VehiculoDTO;
import com.mecamotors.api.entities.models.Vehiculo;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehiculoMapper {

    // De Entidad a DTO
    @Mapping(source = "cliente.id", target = "clienteId")
    @Mapping(source = "cliente.nombre", target = "nombreCliente")
    VehiculoDTO toDto(Vehiculo vehiculo);

    // Para listas
    List<VehiculoDTO> toDtoList(List<Vehiculo> vehiculos);

    // De DTO a Entidad (Para crear/actualizar)
    @Mapping(target = "cliente", ignore = true) // Lo buscaremos en el servicio por ID
    @Mapping(target = "id", ignore = true) // El ID se genera automático
    Vehiculo toEntity(VehiculoDTO dto);

    // Para actualizar un vehículo existente sin borrar campos nulos
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(VehiculoDTO dto, @MappingTarget Vehiculo entity);
}