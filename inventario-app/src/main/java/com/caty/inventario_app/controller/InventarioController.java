package com.caty.inventario_app.controller;

import com.caty.inventario_app.entity.DetalleInventario;
import com.caty.inventario_app.entity.Inventario;
import com.caty.inventario_app.service.DetalleInventarioService;
import com.caty.inventario_app.service.InventarioService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService inventarioService;
    private final DetalleInventarioService detalleInventarioService;

    public InventarioController(InventarioService inventarioService, DetalleInventarioService detalleInventarioService) {
        this.inventarioService = inventarioService;
        this.detalleInventarioService = detalleInventarioService;
    }

    @GetMapping("/activo")
    public Optional<Inventario> obtenerInventarioActivo() {
        return inventarioService.obtenerInventarioEnProgreso();
    }

    @PostMapping("/iniciar")
    public Inventario iniciarInventario(@RequestParam LocalDate fecha) {
        return inventarioService.iniciarInventario(fecha);
    }

    @PostMapping("/{id}/finalizar")
    public Inventario finalizarInventario(@PathVariable Long id) {
        Inventario inventario = inventarioService
                .obtenerInventarioEnProgreso()
                .orElseThrow(()-> new RuntimeException(("No hay inventario en progreso.")) );

        return inventarioService.finalizarInventario(inventario);
    }

    @GetMapping("/activo/detalle")
    public List<DetalleInventario> verDetalleInventarioActivo() {
        Inventario inventario = inventarioService
                .obtenerInventarioEnProgreso()
                .orElseThrow(() -> new RuntimeException("No hay inventario en progreso"));

        return detalleInventarioService.listarPorInventario(inventario);
    }
}
