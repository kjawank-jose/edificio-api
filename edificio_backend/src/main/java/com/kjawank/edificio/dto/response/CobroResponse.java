package com.kjawank.edificio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CobroResponse {
    private String codigoDpto;
    private String nombreDpto;
    private String propietario;
    private Boolean tieneLuz;
    private Boolean tieneLavadero;  // NUEVO

    // Agua
    private Double lecturaAguaActual;
    private Double consumoAguaM3;
    private Double porcentajeAgua;
    private Double pagoAgua;

    // Lavadero (NUEVO)
    private Double lecturaLavaderoActual;
    private Double consumoLavaderoM3;

    // Luz
    private Double lecturaLuzActual;
    private Double consumoLuzKwh;
    private Double porcentajeLuz;
    private Double pagoLuz;

    // Totales
    private Double pagoAreaComun;
    private Double pagoTotal;

    private String mensajeWhatsapp;
}