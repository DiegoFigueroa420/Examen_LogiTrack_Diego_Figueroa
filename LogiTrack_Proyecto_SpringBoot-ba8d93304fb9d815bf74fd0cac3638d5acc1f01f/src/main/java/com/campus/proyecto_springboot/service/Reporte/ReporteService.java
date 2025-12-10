package com.campus.proyecto_springboot.service.reportes;

import com.campus.proyecto_springboot.model.MovimientoInventario;
import com.campus.proyecto_springboot.model.Auditoria;
import com.campus.proyecto_springboot.model.TipoMovimiento;

import java.time.LocalDateTime;
import java.util.List;

public interface ReporteService {
    List<MovimientoInventario> obtenerMovimientos(Long bodegaId, Long productoId, TipoMovimiento tipoMovimiento,
                                                  LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<Auditoria> obtenerAuditorias(Long productoId, String campo, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
