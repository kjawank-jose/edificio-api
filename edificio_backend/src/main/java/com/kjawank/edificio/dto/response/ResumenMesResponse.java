package com.kjawank.edificio.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ResumenMesResponse {

    private String periodo;
    private String nombreMes;
    private String estado;

    // Recibos
    private Double sedapalM3;
    private Double sedapalImporte;
    private Double luzKwh;
    private Double luzImporte;
    private Double luzCostoPorKwh;
    private Double areaComunTotal;
    private Double areaComunPorDpto;

    // Totales
    private Double totalRecaudadoAgua;
    private Double totalRecaudadoLuz;
    private Double totalGeneral;

    private LocalDate fechaCierre;

    private List<CobroResponse> cobros;

}