package com.caty.inventario_app.service;

import com.caty.inventario_app.entity.Producto;
import com.caty.inventario_app.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Optional<Producto> buscarPorCodigoBarra(String codigoBarra) {
        return productoRepository.findByCodigoBarra(codigoBarra);
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }
}
