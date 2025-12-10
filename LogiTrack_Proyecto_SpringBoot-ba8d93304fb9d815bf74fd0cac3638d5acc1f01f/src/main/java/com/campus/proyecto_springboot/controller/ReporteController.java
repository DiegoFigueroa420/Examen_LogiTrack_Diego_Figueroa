package com.campus.proyecto_springboot.controller;

import com.campus.proyecto_springboot.model.MovimientoInventario;
import com.campus.proyecto_springboot.model.Auditoria;
import com.campus.proyecto_springboot.model.TipoMovimiento;
import com.campus.proyecto_springboot.service.reportes.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReporteController {

    private final ReporteService reporteService;

    // Movimientos
    @GetMapping("/movimientos")
    public List<MovimientoInventario> obtenerMovimientos(
            @RequestParam(required = false) Long bodegaId,
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) TipoMovimiento tipoMovimiento,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin
    ) {
        return reporteService.obtenerMovimientos(bodegaId, productoId, tipoMovimiento, fechaInicio, fechaFin);
    }

    // Auditoría
    @GetMapping("/auditoria")
    public List<Auditoria> obtenerAuditoria(
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) String campo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin
    ) {
        return reporteService.obtenerAuditorias(productoId, campo, fechaInicio, fechaFin);
    }
}
