package com.caty.inventario_app.dto;

public record CrearProductoDTO(
        String codigoBarra,
        String nombre,
        Integer unidadesPorCaja
) {
}
