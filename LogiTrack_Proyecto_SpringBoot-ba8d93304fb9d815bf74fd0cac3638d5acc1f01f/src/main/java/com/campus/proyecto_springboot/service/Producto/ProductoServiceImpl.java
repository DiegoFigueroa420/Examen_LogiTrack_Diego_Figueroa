package com.campus.proyecto_springboot.service.Producto;

import com.campus.proyecto_springboot.model.Producto;
import com.campus.proyecto_springboot.model.Usuario;
import com.campus.proyecto_springboot.repository.ProductoRepository;
import com.campus.proyecto_springboot.repository.UsuarioRepository;
import com.campus.proyecto_springboot.security.CurrentUserService;
import com.campus.proyecto_springboot.service.Auditoria.AuditoriaContextService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService{
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private AuditoriaContextService auditoriaContextService;


    @Override
    public List<Producto> findAll(){
        return productoRepository.findAll();
    }

    @Override
    public Producto findById(Long id){
        return productoRepository.findById(id).orElse(null);
    }

    @Override
    public Producto findByNombre(String nombre){
        return productoRepository.findByNombre(nombre);
    }

    @Override
    @Transactional
    public Producto save(Producto producto){
        Long userId = currentUserService.getCurrentUserId();
        auditoriaContextService.setUsuarioActual(userId);
        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public void deleteById(Long id){
        Long userId = currentUserService.getCurrentUserId();
        auditoriaContextService.setUsuarioActual(userId);
        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> findByStockLessThan(Integer limite) {
        return productoRepository.findByStockLessThan(limite);
    }
}
