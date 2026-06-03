package com.kjawank.edificio.repository;

import com.kjawank.edificio.entity.LecturaConsumo;
import com.kjawank.edificio.entity.MesFacturacion;
import com.kjawank.edificio.entity.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface LecturaConsumoRepository extends JpaRepository<LecturaConsumo, Long> {

    List<LecturaConsumo> findByMes(MesFacturacion mes);

    Optional<LecturaConsumo> findByMesAndDepartamento(MesFacturacion mes, Departamento departamento);

    // Historial de un departamento ordenado por periodo
    @Query("SELECT l FROM LecturaConsumo l WHERE l.departamento.id = :dptoId " +
            "ORDER BY l.mes.periodo DESC")
    List<LecturaConsumo> findHistorialByDepartamento(@Param("dptoId") Long dptoId);

    // Últimos N meses para estadísticas
    @Query("SELECT l FROM LecturaConsumo l WHERE l.departamento.id = :dptoId " +
            "ORDER BY l.mes.periodo DESC LIMIT :n")
    List<LecturaConsumo> findUltimosNMesesByDepartamento(
            @Param("dptoId") Long dptoId, @Param("n") int n);
}