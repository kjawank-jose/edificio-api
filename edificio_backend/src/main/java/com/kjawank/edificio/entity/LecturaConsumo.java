package com.kjawank.edificio.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lecturas_consumo",
        uniqueConstraints = @UniqueConstraint(columnNames = {"mes_facturacion_id", "departamento_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LecturaConsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mes_facturacion_id", nullable = false)
    private com.kjawank.edificio.entity.MesFacturacion mes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id", nullable = false)
    private com.kjawank.edificio.entity.Departamento departamento;

    // Lecturas de medidor (acumuladas)
    @Column(nullable = false)
    private Double lecturaAguaActual;
    private Double lecturaLuzActual;    // null para pisos 1-4

    // Consumo del mes (calculado: actual - anterior)
    private Double consumoAguaM3;
    private Double consumoLuzKwh;

    // Porcentaje respecto al total del edificio
    private Double porcentajeAgua;
    private Double porcentajeLuz;

    // Pagos calculados
    private Double pagoAgua;
    private Double pagoLuz;
    private Double pagoAreaComun;
    private Double pagoTotal;
}