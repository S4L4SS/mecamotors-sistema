package com.mecamotors.api.mappers;

import com.mecamotors.api.dtos.RepuestoDTO;
import com.mecamotors.api.entities.models.Repuesto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RepuestoMapper {
    RepuestoDTO toDto(Repuesto repuesto);
    Repuesto toEntity(RepuestoDTO dto);
    List<RepuestoDTO> toDtoList(List<Repuesto> repuestos);
}