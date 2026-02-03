package com.caty.inventario_app.service;

import com.caty.inventario_app.dto.DetalleInventarioDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReporteService {

    public byte[] generarReporteExcel(List<DetalleInventarioDTO> detalles) throws IOException {
        // 1. Crear un libro de trabajo en memoria (como abrir Excel)
        try (Workbook workbook = new XSSFWorkbook()) {

            // 2. Crear una hoja
            Sheet sheet = workbook.createSheet("Inventario");

            // 3. Crear la fila de encabezados (Cabecera)
            Row headerRow = sheet.createRow(0);
            String[] columnas = {"Código de Barras", "Producto", "Cantidad Contada"};

            // Estilo para que se vea bonito (Negrita)
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            // Llenar los encabezados
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // 4. Llenar los datos (Filas de productos)
            int rowIdx = 1;
            for (DetalleInventarioDTO detalle : detalles) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(detalle.codigoBarra());
                row.createCell(1).setCellValue(detalle.nombreProducto());
                row.createCell(2).setCellValue(detalle.cantidad());
            }

            // 5. Ajustar el ancho de las columnas automáticamente
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 6. Escribir el libro en un flujo de bytes (para enviarlo por red)
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}