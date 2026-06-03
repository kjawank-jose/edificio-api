package com.kjawank.edificio.repository;

import com.kjawank.edificio.entity.MesFacturacion;
import com.kjawank.edificio.entity.MesFacturacion.EstadoMes;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MesFacturacionRepository extends JpaRepository<MesFacturacion, Long> {
    Optional<MesFacturacion> findByPeriodo(String periodo);
    Optional<MesFacturacion> findByEstado(EstadoMes estado);
    List<MesFacturacion> findAllByOrderByPeriodoDesc();
    boolean existsByPeriodo(String periodo);
}