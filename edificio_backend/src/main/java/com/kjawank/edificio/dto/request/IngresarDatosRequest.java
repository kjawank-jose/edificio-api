package com.kjawank.edificio.dto.request;

import jakarta.validation.constraints.NotNull;  // ✅ Cambiado de javax a jakarta
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngresarDatosRequest {

    @NotNull
    private String periodo;

    @NotNull
    private Double sedapalM3;

    @NotNull
    private Double sedapalImporte;

    @NotNull
    private Double luzKwh;

    @NotNull
    private Double luzImporte;


    private Map<String, Double> lecturasAgua;

    private Map<String, Double> lecturasLuz;  // Solo para deptos con tieneLuz = true

    private Map<String, Double> lecturasLavadero;  // Lecturas de lavaderos externos
}