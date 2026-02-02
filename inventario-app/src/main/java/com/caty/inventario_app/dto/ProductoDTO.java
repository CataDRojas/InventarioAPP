package com.caty.inventario_app.dto;

import com.caty.inventario_app.entity.Producto;

public record ProductoDTO(
        String codigoBarra,
        String nombre,
        Integer unidadesPorCaja,
        Boolean activo
) {
    public static ProductoDTO fromEntity(Producto producto) {
        return new ProductoDTO(
                producto.getCodigoBarra(),
                producto.getNombre(),
                producto.getUnidadesPorCaja(),
                producto.getActivo()
        );
    }
}
