package com.kjawank.edificio.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CerrarMesRequest {
    @NotBlank
    private String periodoActual;     // "2026-04" — el mes que se cierra
    @NotBlank
    private String proximoPeriodo;    // "2026-05"
    @NotBlank
    private String proximoNombre;     // "Mayo 2026"
}
