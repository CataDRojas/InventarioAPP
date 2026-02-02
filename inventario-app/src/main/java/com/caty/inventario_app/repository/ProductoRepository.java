package com.caty.inventario_app.repository;

import com.caty.inventario_app.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigoBarra(String codigoBarra);
}
