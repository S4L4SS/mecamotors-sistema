package com.mecamotors.api.mappers;

import com.mecamotors.api.dtos.CitaDTO;
import com.mecamotors.api.entities.models.Cita;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CitaMapper {

    @Mapping(source = "cliente.id", target = "clienteId")
    @Mapping(source = "cliente.nombre", target = "clienteNombre") // Asegurate de tener este campo en tu DTO
    @Mapping(source = "vehiculo.id", target = "vehiculoId")
    @Mapping(source = "vehiculo.placa", target = "vehiculoPlaca")
    CitaDTO toDto(Cita cita);

    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "vehiculo", ignore = true)
    Cita toEntity(CitaDTO dto);

    List<CitaDTO> toDtoList(List<Cita> citas);
}