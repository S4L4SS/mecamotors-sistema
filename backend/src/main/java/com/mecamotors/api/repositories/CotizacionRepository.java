package com.mecamotors.api.repositories;

import com.mecamotors.api.entities.models.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {
    List<Cotizacion> findByClienteId(Long clienteId);
    List<Cotizacion> findByEstadoOrderByFechaDesc(String estado);
    List<Cotizacion> findAllByOrderByFechaDesc();
}
