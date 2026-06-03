package com.kjawank.edificio.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AbrirMesRequest {

    @NotBlank
    private String periodo;      // 2026-05

    @NotBlank
    private String nombreMes;    // Mayo 2026
}
