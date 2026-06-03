package com.kjawank.edificio.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "departamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;          // "101", "201", "501", etc.

    @Column(nullable = false)
    private String nombre;          // "1° Piso", "5°A – Nohelia"

    @Column(nullable = false)
    private String propietario;     // nombre del inquilino

    @Column(nullable = false)
    private Boolean tieneLuz;       // solo pisos 5 y 6

    @Column(nullable = false)
    private Integer piso;

    // Última lectura conocida (se actualiza al cerrar mes)
    @Column(nullable = false, precision = 10)
    private Double ultimaLecturaAgua;

    private Double ultimaLecturaLuz;   // null para pisos 1-4
}