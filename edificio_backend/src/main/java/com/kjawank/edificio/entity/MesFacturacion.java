package com.kjawank.edificio.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "meses_facturacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MesFacturacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String periodo;             // "2026-04" (año-mes, para ordenar)

    @Column(nullable = false, length = 30)
    private String nombreMes;           // "Abril 2026" (para mostrar)

    // Recibo SEDAPAL
    @Column(nullable = false)
    private Double sedapalM3;
    @Column(nullable = false)
    private Double sedapalImporte;

    // Recibo luz (pisos 5-6)
    @Column(nullable = false)
    private Double luzKwh;
    @Column(nullable = false)
    private Double luzImporte;

    // Calculados automáticamente
    private Double luzCostoPorKwh;
    private Double areaComun;           // fijo S/22, dividido entre 8
    private Double areaComunPorDpto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoMes estado;           // ABIERTO, CERRADO

    private LocalDate fechaCierre;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;

    @PrePersist
    protected void onCreate() { creadoEn = LocalDateTime.now(); actualizadoEn = LocalDateTime.now(); }
    @PreUpdate
    protected void onUpdate() { actualizadoEn = LocalDateTime.now(); }

    public enum EstadoMes { ABIERTO, CERRADO }
}