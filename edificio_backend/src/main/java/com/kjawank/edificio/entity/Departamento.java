package com.kjawank.edificio.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departamentos")
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    private String propietario;

    private String telefono;

    private String email;

    @Column(name = "piso")
    private Integer piso;

    @Column(name = "tiene_luz")
    private Boolean tieneLuz;

    @Column(name = "tiene_lavadero")
    private Boolean tieneLavadero;

    @Column(name = "ultima_lectura_agua")
    private Double ultimaLecturaAgua = 0.0;

    @Column(name = "ultima_lectura_luz")
    private Double ultimaLecturaLuz = 0.0;

    @Column(name = "ultima_lectura_lavadero")
    private Double ultimaLecturaLavadero = 0.0;

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL)
    private List<LecturaConsumo> lecturas = new ArrayList<>();

    // Constructores
    public Departamento() {}

    public Departamento(String codigo, String nombre, String propietario, Integer piso,
                        Boolean tieneLuz, Boolean tieneLavadero,
                        Double ultimaLecturaAgua, Double ultimaLecturaLuz, Double ultimaLecturaLavadero) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.propietario = propietario;
        this.piso = piso;
        this.tieneLuz = tieneLuz;
        this.tieneLavadero = tieneLavadero;
        this.ultimaLecturaAgua = ultimaLecturaAgua;
        this.ultimaLecturaLuz = ultimaLecturaLuz;
        this.ultimaLecturaLavadero = ultimaLecturaLavadero;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPropietario() { return propietario; }
    public void setPropietario(String propietario) { this.propietario = propietario; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getPiso() { return piso; }
    public void setPiso(Integer piso) { this.piso = piso; }

    public Boolean getTieneLuz() { return tieneLuz; }
    public void setTieneLuz(Boolean tieneLuz) { this.tieneLuz = tieneLuz; }

    public Boolean getTieneLavadero() { return tieneLavadero; }
    public void setTieneLavadero(Boolean tieneLavadero) { this.tieneLavadero = tieneLavadero; }

    public Double getUltimaLecturaAgua() { return ultimaLecturaAgua; }
    public void setUltimaLecturaAgua(Double ultimaLecturaAgua) { this.ultimaLecturaAgua = ultimaLecturaAgua; }

    public Double getUltimaLecturaLuz() { return ultimaLecturaLuz; }
    public void setUltimaLecturaLuz(Double ultimaLecturaLuz) { this.ultimaLecturaLuz = ultimaLecturaLuz; }

    public Double getUltimaLecturaLavadero() { return ultimaLecturaLavadero; }
    public void setUltimaLecturaLavadero(Double ultimaLecturaLavadero) { this.ultimaLecturaLavadero = ultimaLecturaLavadero; }

    public List<LecturaConsumo> getLecturas() { return lecturas; }
    public void setLecturas(List<LecturaConsumo> lecturas) { this.lecturas = lecturas; }
}