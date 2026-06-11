package com.kjawank.edificio.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "meses_facturacion")
public class MesFacturacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String periodo;

    private String nombreMes;

    private Double sedapalM3;
    private Double sedapalImporte;
    private Double luzKwh;
    private Double luzImporte;
    private Double luzCostoPorKwh;
    private Double areaComun;
    private Double areaComunPorDpto;

    @Enumerated(EnumType.STRING)
    private EstadoMes estado;

    private LocalDate fechaCierre;

    public enum EstadoMes {
        ABIERTO, CERRADO
    }

    // Constructores
    public MesFacturacion() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public String getNombreMes() { return nombreMes; }
    public void setNombreMes(String nombreMes) { this.nombreMes = nombreMes; }

    public Double getSedapalM3() { return sedapalM3; }
    public void setSedapalM3(Double sedapalM3) { this.sedapalM3 = sedapalM3; }

    public Double getSedapalImporte() { return sedapalImporte; }
    public void setSedapalImporte(Double sedapalImporte) { this.sedapalImporte = sedapalImporte; }

    public Double getLuzKwh() { return luzKwh; }
    public void setLuzKwh(Double luzKwh) { this.luzKwh = luzKwh; }

    public Double getLuzImporte() { return luzImporte; }
    public void setLuzImporte(Double luzImporte) { this.luzImporte = luzImporte; }

    public Double getLuzCostoPorKwh() { return luzCostoPorKwh; }
    public void setLuzCostoPorKwh(Double luzCostoPorKwh) { this.luzCostoPorKwh = luzCostoPorKwh; }

    public Double getAreaComun() { return areaComun; }
    public void setAreaComun(Double areaComun) { this.areaComun = areaComun; }

    public Double getAreaComunPorDpto() { return areaComunPorDpto; }
    public void setAreaComunPorDpto(Double areaComunPorDpto) { this.areaComunPorDpto = areaComunPorDpto; }

    public EstadoMes getEstado() { return estado; }
    public void setEstado(EstadoMes estado) { this.estado = estado; }

    public LocalDate getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDate fechaCierre) { this.fechaCierre = fechaCierre; }
}