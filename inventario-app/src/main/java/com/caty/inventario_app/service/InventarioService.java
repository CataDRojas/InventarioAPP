package com.caty.inventario_app.service;

import com.caty.inventario_app.entity.EstadoInventario;
import com.caty.inventario_app.entity.Inventario;
import com.caty.inventario_app.repository.InventarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    public Optional<Inventario> obtenerInventarioEnProgreso() {
        return inventarioRepository.findByEstado(EstadoInventario.EN_PROGRESO);
    }

    public Inventario iniciarInventario(LocalDate fecha) {
        //solo un inventario en progreso
        Optional<Inventario> existente = obtenerInventarioEnProgreso();
        if (existente.isPresent()) {
            return existente.get();
        }
        Inventario inventario = new Inventario(fecha);
        return inventarioRepository.save(inventario);
    }

    public Inventario finalizarInventario(Inventario inventario) {
        inventario.setEstado(EstadoInventario.FINALIZADO);
        inventario.setFechaFinalizacion(LocalDateTime.now());;
        return inventarioRepository.save(inventario);
    }

}
