package com.caty.inventario_app.controller;

import com.caty.inventario_app.entity.Inventario;
import com.caty.inventario_app.entity.Producto;
import com.caty.inventario_app.service.DetalleInventarioService;
import com.caty.inventario_app.service.InventarioService;
import com.caty.inventario_app.service.ProductoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public String escanearProducto(
            @RequestParam String codigoBarra,
            @RequestParam Integer cantidad
    ) {
        Inventario inventario = inventarioService
                .obtenerInventarioEnProgreso()
                .orElseThrow(() -> new RuntimeException("No hay inventario activo"));

        Producto producto = productoService
                .buscarPorCodigoBarra(codigoBarra)
                .orElseThrow(()-> new RuntimeException("Producto no encontrado"));

        detalleInventarioService.agregarOActualizar(inventario, producto, cantidad);

        return "Producto agregado al inventario";
    }
}
