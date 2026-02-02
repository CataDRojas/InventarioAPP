package com.caty.inventario_app.dto;

import com.caty.inventario_app.entity.Inventario;

import java.time.LocalDate;

public record InventarioDTO(
        Long id,
        LocalDate fecha,
        String estado // EN PROCESO o FINALIZADO
) {
    public static InventarioDTO fromEntity(Inventario inventario) {
        return new InventarioDTO(
                inventario.getId(),
                inventario.getFecha(),
                inventario.getEstado().toString() //Convertir enum a string
        );
    }
}
