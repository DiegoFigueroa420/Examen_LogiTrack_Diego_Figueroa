package com.campus.proyecto_springboot.service.MovimientoInventario;

import com.campus.proyecto_springboot.model.MovimientoInventario;
import com.campus.proyecto_springboot.model.Producto;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoInventarioService {
    List<MovimientoInventario> findAll();
    MovimientoInventario findById(Long id);
    MovimientoInventario save(MovimientoInventario movimientoInventario);
    void deleteById(Long id);
    List<MovimientoInventario> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta);
}
