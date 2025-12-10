package com.campus.proyecto_springboot.controller;

import com.campus.proyecto_springboot.exception.ResourceNotFoundException;
import com.campus.proyecto_springboot.model.MovimientoInventario;
import com.campus.proyecto_springboot.service.MovimientoInventario.MovimientoInventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/movimientoInventario")
public class MovimientoInventarioController {

    @Autowired
    private MovimientoInventarioService movimientoInventarioService;

    // Listar todos los movimientos
    @GetMapping
    public List<MovimientoInventario> listarMovimientos() {
        return movimientoInventarioService.findAll();  // <-- Se serializa a JSON automáticamente
    }

    // Buscar movimiento por ID
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoInventario> buscarPorId(@PathVariable Long id) {
        MovimientoInventario movimiento = movimientoInventarioService.findById(id);

        if (movimiento == null) {
            throw new ResourceNotFoundException("Movimiento de inventario no encontrado con id: " + id);
        }

        return ResponseEntity.ok(movimiento);
    }

    // Registrar un nuevo movimiento de inventario (aplica lógica de stock)
    @PostMapping
    public ResponseEntity<MovimientoInventario> crearMovimiento(
            @RequestBody MovimientoInventario movimiento) {

        MovimientoInventario creado = movimientoInventarioService.save(movimiento);
        return ResponseEntity.ok(creado);  // <-- Spring/Jackson lo serializa a JSON válido
    }

    /**
     * Actualización "ligera" de un movimiento existente.
     * - No se cambia tipoMovimiento ni detalles (para no desbalancear stock).
     * - Útil si necesitas corregir la fecha o las bodegas.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoInventario> actualizar(
            @PathVariable Long id,
            @RequestBody MovimientoInventario movimientoInventario) {

        MovimientoInventario existente = movimientoInventarioService.findById(id);

        if (existente == null) {
            throw new ResourceNotFoundException("Movimiento de inventario no encontrado con id: " + id);
        }

        // Forzamos el id para que el service sepa que es actualización
        movimientoInventario.setId(id);
        MovimientoInventario actualizado = movimientoInventarioService.save(movimientoInventario);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar movimiento por ID (no recalcula stock)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        MovimientoInventario existente = movimientoInventarioService.findById(id);

        if (existente == null) {
            throw new ResourceNotFoundException("Movimiento de inventario no encontrado con id: " + id);
        }

        movimientoInventarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Movimientos por rango de fechas
    @GetMapping("/por-fecha")
    public ResponseEntity<List<MovimientoInventario>> listarPorRango(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime desde,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime hasta) {

        List<MovimientoInventario> movimientos =
                movimientoInventarioService.findByFechaBetween(desde, hasta);
        return ResponseEntity.ok(movimientos);
    }
}
