package com.caty.inventario_app.controller;

import com.caty.inventario_app.dto.DetalleInventarioDTO;
import com.caty.inventario_app.dto.InventarioDTO;
import com.caty.inventario_app.entity.DetalleInventario;
import com.caty.inventario_app.entity.Inventario;
import com.caty.inventario_app.exception.ResourceNotFoundException;
import com.caty.inventario_app.service.DetalleInventarioService;
import com.caty.inventario_app.service.InventarioService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Optional<InventarioDTO> obtenerInventarioActivo() {
        return inventarioService.obtenerInventarioEnProgreso()
                .map(InventarioDTO::fromEntity);
    }

    @PostMapping("/iniciar")
    public InventarioDTO iniciarInventario(@RequestParam LocalDate fecha) {
        Inventario inventario = inventarioService.iniciarInventario(fecha);
        return InventarioDTO.fromEntity(inventario);
    }

    @GetMapping("/activo/detalle")
    public List<DetalleInventarioDTO> verDetalleInventarioActivo() {
        Inventario inventario = inventarioService
                .obtenerInventarioEnProgreso()
                .orElseThrow(() -> new ResourceNotFoundException("No hay inventario en progreso"));

        return detalleInventarioService.listarPorInventario(inventario)
                .stream()
                .map(DetalleInventarioDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/finalizar")
    public InventarioDTO finalizarInventario(@PathVariable Long id) {
        Inventario inventario = inventarioService
                .obtenerInventarioEnProgreso()
                .orElseThrow(()-> new ResourceNotFoundException(("No hay inventario en progreso.")) );

        Inventario finalizado = inventarioService.finalizarInventario(inventario);
        return InventarioDTO.fromEntity(finalizado);
    }
}
