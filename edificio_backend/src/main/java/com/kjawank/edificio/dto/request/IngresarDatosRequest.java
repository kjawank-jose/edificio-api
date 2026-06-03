package com.kjawank.edificio.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Map;

@Data
public class IngresarDatosRequest {

    @NotBlank
    private String periodo;

    // Recibo SEDAPAL
    @NotNull
    @Positive
    private Double sedapalM3;

    @NotNull
    @Positive
    private Double sedapalImporte;

    // Recibo Luz
    @NotNull
    @Positive
    private Double luzKwh;

    @NotNull
    @Positive
    private Double luzImporte;

    // Lecturas de agua por departamento
    @NotNull
    private Map<String, Double> lecturasAgua;

    // Lecturas de luz por departamento
    @NotNull
    private Map<String, Double> lecturasLuz;
}