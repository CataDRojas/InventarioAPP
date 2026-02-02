package com.caty.inventario_app.dto;

import com.caty.inventario_app.entity.DetalleInventario;

public record DetalleInventarioDTO(
        String codigoBarra,
        String nombreProducto,
        Integer cantidad
) {
    public static DetalleInventarioDTO fromEntity(DetalleInventario detalle) {
        return new DetalleInventarioDTO(
                detalle.getProducto().getCodigoBarra(),
                detalle.getProducto().getNombre(),
                detalle.getCantidad()
        );
    }
}
