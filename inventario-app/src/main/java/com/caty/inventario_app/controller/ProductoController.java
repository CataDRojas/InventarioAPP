package com.caty.inventario_app.controller;

import com.caty.inventario_app.dto.CrearProductoDTO;
import com.caty.inventario_app.dto.ProductoDTO;
import com.caty.inventario_app.dto.DetalleInventarioDTO;
import com.caty.inventario_app.entity.DetalleInventario;
import com.caty.inventario_app.entity.Inventario;
import com.caty.inventario_app.entity.Producto;
import com.caty.inventario_app.exception.ResourceNotFoundException;
import com.caty.inventario_app.service.DetalleInventarioService;
import com.caty.inventario_app.service.InventarioService;
import com.caty.inventario_app.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final InventarioService inventarioService;
    private final DetalleInventarioService detalleInventarioService;

    public ProductoController(
            ProductoService productoService,
            InventarioService inventarioService,
            DetalleInventarioService detalleInventarioService
    ) {
        this.productoService = productoService;
        this.inventarioService = inventarioService;
        this.detalleInventarioService = detalleInventarioService;
    }

    @PostMapping("/scan")
    public ResponseEntity<DetalleInventarioDTO> escanearProducto(
            @RequestParam String codigoBarra,
            @RequestParam Integer cantidad
    ) {
        Inventario inventario = inventarioService
                .obtenerInventarioEnProgreso()
                .orElseThrow(() -> new ResourceNotFoundException("No hay inventario activo."));

        Producto producto = productoService
                .buscarPorCodigoBarra(codigoBarra)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con código: " + codigoBarra));

        DetalleInventario detalleActualizado = detalleInventarioService
                .agregarOActualizar(inventario, producto, cantidad);

        DetalleInventarioDTO respuesta = DetalleInventarioDTO.fromEntity(detalleActualizado);

        return ResponseEntity.ok(respuesta);
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody CrearProductoDTO datos) {
        Producto productoGuardado = productoService.crearProducto(datos);
        return ResponseEntity.ok(ProductoDTO.fromEntity(productoGuardado));
    }
}
