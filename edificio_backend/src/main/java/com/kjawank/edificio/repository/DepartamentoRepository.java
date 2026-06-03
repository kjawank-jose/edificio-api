package com.kjawank.edificio.repository;

import com.kjawank.edificio.entity.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    Optional<Departamento> findByCodigo(String codigo);
}