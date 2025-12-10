package com.campus.proyecto_springboot.repository;

import com.campus.proyecto_springboot.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

    @Query("""
           SELECT a FROM Auditoria a
           WHERE (:productoId IS NULL OR (a.producto IS NOT NULL AND a.producto.id = :productoId))
             AND (:campo IS NULL OR LOWER(a.campoModificado) = LOWER(:campo))
             AND (:fechaInicio IS NULL OR a.fechaCambio >= :fechaInicio)
             AND (:fechaFin IS NULL OR a.fechaCambio <= :fechaFin)
           ORDER BY a.fechaCambio DESC
           """)
    List<Auditoria> buscarAuditoriaConFiltros(
            @Param("productoId") Long productoId,
            @Param("campo") String campo,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
}
