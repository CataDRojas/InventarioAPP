package com.caty.inventario_app.service;

import com.caty.inventario_app.entity.DetalleInventario;
import com.caty.inventario_app.entity.Inventario;
import com.caty.inventario_app.entity.Producto;
import com.caty.inventario_app.repository.DetalleInventarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleInventarioService {

    private final DetalleInventarioRepository detalleInventarioRepository;

    public DetalleInventarioService(DetalleInventarioRepository detalleRepository) {
        this.detalleInventarioRepository = detalleRepository;
    }

    public DetalleInventario agregarOActualizar(
            Inventario inventario,
            Producto producto,
            Integer cantidad
    ) {
        return detalleInventarioRepository
                .findByInventarioAndProducto(inventario, producto)
                .map(detalle -> {
                    detalle.setCantidad(detalle.getCantidad() + cantidad);
                    return detalleInventarioRepository.save(detalle);
                })
                .orElseGet(() -> {
                    DetalleInventario nuevo = new DetalleInventario(
                            inventario,
                            producto,
                            cantidad
                    );
                    return detalleInventarioRepository.save(nuevo);
                });
    }

    public List<DetalleInventario> listarPorInventario(Inventario inventario) {
        return detalleInventarioRepository.findByInventario(inventario);
    }

}
