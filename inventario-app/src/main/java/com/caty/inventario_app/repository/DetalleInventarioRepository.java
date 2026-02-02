package com.caty.inventario_app.repository;

import com.caty.inventario_app.entity.DetalleInventario;
import com.caty.inventario_app.entity.Inventario;
import com.caty.inventario_app.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DetalleInventarioRepository extends JpaRepository<DetalleInventario, Long> {

    Optional<DetalleInventario> findByInventarioAndProducto(
            Inventario inventario,
            Producto producto
    );

    List<DetalleInventario> findByInventario(Inventario inventario);


}
