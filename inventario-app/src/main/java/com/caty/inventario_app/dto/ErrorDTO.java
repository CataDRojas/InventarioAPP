package com.caty.inventario_app.dto;

import java.time.LocalDateTime;

public record ErrorDTO(
        String mensaje,
        String error,
        int estado,
        LocalDateTime fecha
) {
}
