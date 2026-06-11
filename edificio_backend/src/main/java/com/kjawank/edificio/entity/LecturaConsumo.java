package com.kjawank.edificio.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lecturas_consumo")
public class LecturaConsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mes_id", nullable = false)
    private MesFacturacion mes;

    @ManyToOne
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    private Double lecturaAguaActual;
    private Double consumoAguaM3;
    private Double porcentajeAgua;
    private Double pagoAgua;

    private Double lecturaLavaderoActual;
    private Double consumoLavaderoM3;

    private Double lecturaLuzActual;
    private Double consumoLuzKwh;
    private Double porcentajeLuz;
    private Double pagoLuz;

    private Double pagoAreaComun;
    private Double pagoTotal;

    // Constructores
    public LecturaConsumo() {}

    public LecturaConsumo(MesFacturacion mes, Departamento departamento) {
        this.mes = mes;
        this.departamento = departamento;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MesFacturacion getMes() { return mes; }
    public void setMes(MesFacturacion mes) { this.mes = mes; }

    public Departamento getDepartamento() { return departamento; }
    public void setDepartamento(Departamento departamento) { this.departamento = departamento; }

    public Double getLecturaAguaActual() { return lecturaAguaActual; }
    public void setLecturaAguaActual(Double lecturaAguaActual) { this.lecturaAguaActual = lecturaAguaActual; }

    public Double getConsumoAguaM3() { return consumoAguaM3; }
    public void setConsumoAguaM3(Double consumoAguaM3) { this.consumoAguaM3 = consumoAguaM3; }

    public Double getPorcentajeAgua() { return porcentajeAgua; }
    public void setPorcentajeAgua(Double porcentajeAgua) { this.porcentajeAgua = porcentajeAgua; }

    public Double getPagoAgua() { return pagoAgua; }
    public void setPagoAgua(Double pagoAgua) { this.pagoAgua = pagoAgua; }

    public Double getLecturaLavaderoActual() { return lecturaLavaderoActual; }
    public void setLecturaLavaderoActual(Double lecturaLavaderoActual) { this.lecturaLavaderoActual = lecturaLavaderoActual; }

    public Double getConsumoLavaderoM3() { return consumoLavaderoM3; }
    public void setConsumoLavaderoM3(Double consumoLavaderoM3) { this.consumoLavaderoM3 = consumoLavaderoM3; }

    public Double getLecturaLuzActual() { return lecturaLuzActual; }
    public void setLecturaLuzActual(Double lecturaLuzActual) { this.lecturaLuzActual = lecturaLuzActual; }

    public Double getConsumoLuzKwh() { return consumoLuzKwh; }
    public void setConsumoLuzKwh(Double consumoLuzKwh) { this.consumoLuzKwh = consumoLuzKwh; }

    public Double getPorcentajeLuz() { return porcentajeLuz; }
    public void setPorcentajeLuz(Double porcentajeLuz) { this.porcentajeLuz = porcentajeLuz; }

    public Double getPagoLuz() { return pagoLuz; }
    public void setPagoLuz(Double pagoLuz) { this.pagoLuz = pagoLuz; }

    public Double getPagoAreaComun() { return pagoAreaComun; }
    public void setPagoAreaComun(Double pagoAreaComun) { this.pagoAreaComun = pagoAreaComun; }

    public Double getPagoTotal() { return pagoTotal; }
    public void setPagoTotal(Double pagoTotal) { this.pagoTotal = pagoTotal; }
}