package com.campus.proyecto_springboot.repository;

import com.campus.proyecto_springboot.model.MovimientoInventario;
import com.campus.proyecto_springboot.model.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    // Tu método anterior puede permanecer. Añadimos uno nuevo con parámetros opcionales.
    @Query("""
            SELECT m FROM MovimientoInventario m
            WHERE (:bodegaId IS NULL OR m.bodega.id = :bodegaId)
              AND (:productoId IS NULL OR EXISTS (
                    SELECT d FROM m.detalleMovimientoList d WHERE d.producto.id = :productoId
                  ))
              AND (:tipoMovimiento IS NULL OR m.tipoMovimiento = :tipoMovimiento)
              AND (:fechaInicio IS NULL OR m.fecha >= :fechaInicio)
              AND (:fechaFin IS NULL OR m.fecha <= :fechaFin)
            ORDER BY m.fecha DESC
            """)
    List<MovimientoInventario> buscarConFiltros(
            @Param("bodegaId") Long bodegaId,
            @Param("productoId") Long productoId,
            @Param("tipoMovimiento") TipoMovimiento tipoMovimiento,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
}
