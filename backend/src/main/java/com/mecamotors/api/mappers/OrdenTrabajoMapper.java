package com.mecamotors.api.mappers;

// Paquete: com.mecamotors.api.mappers

import com.mecamotors.api.dtos.OrdenTrabajoDTO;
import com.mecamotors.api.dtos.DetalleOrdenDTO;
import com.mecamotors.api.entities.models.OrdenTrabajo;
import com.mecamotors.api.entities.models.DetalleOrden;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrdenTrabajoMapper {

    // --- Mapeo de la Cabecera ---
    @Mapping(source = "vehiculo.placa", target = "placaVehiculo")
    @Mapping(source = "vehiculo.marca", target = "marcaVehiculo")
    @Mapping(source = "mecanico.username", target = "nombreMecanico") // O nombre real
    @Mapping(source = "totalEstimado", target = "totalEstimado")
    OrdenTrabajoDTO toDto(OrdenTrabajo orden);

    List<OrdenTrabajoDTO> toDtoList(List<OrdenTrabajo> ordenes);

    // --- Mapeo del Detalle (Hijo) ---
    @Mapping(source = "repuesto.id", target = "repuestoId")
    // Si hay repuesto usa su nombre, si no, usa la descripcion manual
    @Mapping(target = "descripcion", expression = "java(detalle.getRepuesto() != null ? detalle.getRepuesto().getNombre() : detalle.getDescripcionServicio())")
    DetalleOrdenDTO detalleToDto(DetalleOrden detalle);

    // --- Inversa (Creación) ---
    @Mapping(target = "fechaIngreso", ignore = true)
    @Mapping(target = "detalles", ignore = true)
    @Mapping(target = "vehiculo", ignore = true)
    @Mapping(target = "mecanico", ignore = true)
    OrdenTrabajo toEntity(OrdenTrabajoDTO dto);
}