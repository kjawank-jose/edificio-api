package com.kjawank.edificio.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CobroResponse {

    private String codigoDpto;
    private String nombreDpto;
    private String propietario;
    private Boolean tieneLuz;

    // Agua
    private Double lecturaAguaActual;
    private Double consumoAguaM3;
    private Double porcentajeAgua;
    private Double pagoAgua;

    // Luz
    private Double lecturaLuzActual;
    private Double consumoLuzKwh;
    private Double porcentajeLuz;
    private Double pagoLuz;

    // Común y total
    private Double pagoAreaComun;
    private Double pagoTotal;

    // WhatsApp
    private String mensajeWhatsapp;

}