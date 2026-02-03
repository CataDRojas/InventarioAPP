package com.caty.inventario_app.controller;

import com.caty.inventario_app.dto.DetalleInventarioDTO;
import com.caty.inventario_app.dto.InventarioDTO;
import com.caty.inventario_app.entity.DetalleInventario;
import com.caty.inventario_app.entity.Inventario;
import com.caty.inventario_app.exception.ResourceNotFoundException;
import com.caty.inventario_app.service.DetalleInventarioService;
import com.caty.inventario_app.service.InventarioService;
import com.caty.inventario_app.service.ReporteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService inventarioService;
    private final DetalleInventarioService detalleInventarioService;
    private final ReporteService reporteService;

    public InventarioController(InventarioService inventarioService, DetalleInventarioService detalleInventarioService, ReporteService reporteService) {
        this.inventarioService = inventarioService;
        this.detalleInventarioService = detalleInventarioService;
        this.reporteService = reporteService;
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

    @GetMapping("/activo/exportar")
    public ResponseEntity<byte[]> exportarInventarioActivo() throws IOException {
        //Buscar inventario
        Inventario inventario = inventarioService
                .obtenerInventarioEnProgreso()
                .orElseThrow(() -> new ResourceNotFoundException("No hay inventario para exportar."));

        //obtener datos y convertirlos a dtos
        List<DetalleInventarioDTO> detalles = detalleInventarioService.listarPorInventario(inventario)
                .stream()
                .map(DetalleInventarioDTO::fromEntity)
                .collect(Collectors.toList());

        //generar el archivo excell en memoria
        byte[] excelBytes = reporteService.generarReporteExcel(detalles);

        //configurar nombre de archivo
        String nombreArchivo = "Inventario_" + inventario.getFecha() + ".xlsx";

        //devolver la respuesta
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }
}
