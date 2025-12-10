package com.campus.proyecto_springboot.service.Reporte;

import com.campus.proyecto_springboot.dto.ProductoMasMovidoDTO;
import com.campus.proyecto_springboot.dto.ResumenGeneralDTO;
import com.campus.proyecto_springboot.dto.StockPorBodegaDTO;
import com.campus.proyecto_springboot.model.DetalleMovimiento;
import com.campus.proyecto_springboot.model.MovimientoInventario;
import com.campus.proyecto_springboot.model.TipoMovimiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Override
    public ResumenGeneralDTO obtenerResumenGeneral() {

        List<MovimientoInventario> movimientos = movimientoInventarioRepository.findAll();

        Map<Long, StockPorBodegaDTO> stockPorBodegaMap = new HashMap<>();
        Map<Long, ProductoMasMovidoDTO> productosMasMovidosMap = new HashMap<>();

        for (MovimientoInventario mov : movimientos) {

            if (mov.getDetalles() == null) continue;

            for (DetalleMovimiento det : mov.getDetalles()) {

                if (det == null || det.getProducto() == null) continue;

                int cantidad = det.getCantidad();
                Long prodId = det.getProducto().getId();
                String prodNombre = det.getProducto().getNombre();

                // ====================================
                // 1. PRODUCTOS MÁS MOVIDOS
                // ====================================
                ProductoMasMovidoDTO prodDTO =
                        productosMasMovidosMap.getOrDefault(
                                prodId, new ProductoMasMovidoDTO(prodId, prodNombre, 0L)
                        );

                prodDTO.setTotalMovido(prodDTO.getTotalMovido() + cantidad);
                productosMasMovidosMap.put(prodId, prodDTO);

                // ====================================
                // 2. STOCK POR BODEGA (NETO)
                // ====================================
                TipoMovimiento tipo = mov.getTipoMovimiento();

                switch (tipo) {

                    case ENTRADA -> {
                        if (mov.getBodegaDestino() != null) {
                            actualizarStockBodega(
                                    stockPorBodegaMap,
                                    mov.getBodegaDestino().getId(),
                                    mov.getBodegaDestino().getNombre(),
                                    cantidad
                            );
                        }
                    }

                    case SALIDA -> {
                        if (mov.getBodegaOrigen() != null) {
                            actualizarStockBodega(
                                    stockPorBodegaMap,
                                    mov.getBodegaOrigen().getId(),
                                    mov.getBodegaOrigen().getNombre(),
                                    -cantidad
                            );
                        }
                    }

                    case AJUSTE -> {
                        // Ajustes pueden ser positivos o negativos
                        if (mov.getBodegaOrigen() != null) {
                            actualizarStockBodega(
                                    stockPorBodegaMap,
                                    mov.getBodegaOrigen().getId(),
                                    mov.getBodegaOrigen().getNombre(),
                                    cantidad  // puede ser positivo o negativo
                            );
                        }
                    }
                }
            }
        }

        // Ordenar los resultados
        List<StockPorBodegaDTO> stockPorBodegaOrdenado =
                stockPorBodegaMap.values().stream()
                        .sorted(Comparator.comparingLong(StockPorBodegaDTO::getStockTotal).reversed())
                        .collect(Collectors.toList());

        List<ProductoMasMovidoDTO> productosMasMovidosOrdenado =
                productosMasMovidosMap.values().stream()
                        .sorted(Comparator.comparingLong(ProductoMasMovidoDTO::getTotalMovido).reversed())
                        .collect(Collectors.toList());

        // Crear DTO final
        ResumenGeneralDTO resumen = new ResumenGeneralDTO();
        resumen.setStockPorBodega(stockPorBodegaOrdenado);
        resumen.setProductosMasMovidos(productosMasMovidosOrdenado);

        return resumen;
    }

    // ====================================
    // MÉTODO AUXILIAR PARA REDUCIR CÓDIGO
    // ====================================
    private void actualizarStockBodega(Map<Long, StockPorBodegaDTO> mapa,
                                       Long bodegaId,
                                       String nombreBodega,
                                       int cantidad) {

        StockPorBodegaDTO dto = mapa.getOrDefault(
                bodegaId,
                new StockPorBodegaDTO(bodegaId, nombreBodega, 0L)
        );

        dto.setStockTotal(dto.getStockTotal() + cantidad);
        mapa.put(bodegaId, dto);
    }
}
