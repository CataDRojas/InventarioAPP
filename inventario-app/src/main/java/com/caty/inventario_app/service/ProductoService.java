package com.caty.inventario_app.service;

import com.caty.inventario_app.dto.CrearProductoDTO;
import com.caty.inventario_app.entity.Producto;
import com.caty.inventario_app.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Producto crearProducto(CrearProductoDTO datos) {
        if (productoRepository.findByCodigoBarra(datos.codigoBarra()).isPresent()) {
            throw new RuntimeException("Ya existe un producto con el código: " + datos.codigoBarra());
        }

        Producto nuevo = new Producto();
        nuevo.setCodigoBarra(datos.codigoBarra());
        nuevo.setNombre(datos.nombre());
        nuevo.setUnidadesPorCaja(datos.unidadesPorCaja());
        nuevo.setActivo(true);

        return productoRepository.save(nuevo);
    }
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }
}
