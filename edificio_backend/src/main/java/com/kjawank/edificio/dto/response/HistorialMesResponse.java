package com.kjawank.edificio.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class HistorialMesResponse {

    private String periodo;
    private String nombreMes;
    private String estado;
    private Double totalGeneral;
    private LocalDate fechaCierre;

}