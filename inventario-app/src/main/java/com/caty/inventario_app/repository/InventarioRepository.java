package com.caty.inventario_app.repository;

import com.caty.inventario_app.entity.EstadoInventario;
import com.caty.inventario_app.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    Optional<Inventario> findByEstado(EstadoInventario estado);

    List<Inventario> findByFechaOrderByFechaCreacionDesc(java.time.LocalDate fecha);
}
