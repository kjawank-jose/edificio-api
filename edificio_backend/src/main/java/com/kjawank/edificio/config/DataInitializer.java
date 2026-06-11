package com.kjawank.edificio.config;

import com.kjawank.edificio.entity.Departamento;
import com.kjawank.edificio.repository.DepartamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final DepartamentoRepository dptoRepo;

    @Override
    public void run(String... args) throws Exception {
        if (dptoRepo.count() == 0) {
            initDepartamentos();
        } else {
            log.info("Los departamentos ya existen. Count: {}", dptoRepo.count());
        }
    }

    private void initDepartamentos() {
        // Lecturas iniciales extraídas del historial (cierre de Abril 2026)
        List<Departamento> dptos = List.of(
                // Pisos 1-4 (sin medidor de luz, sin lavadero)
                Departamento.builder()
                        .codigo("101").nombre("1° Piso")
                        .propietario("Inquilino 101")
                        .piso(1)
                        .tieneLuz(false)
                        .tieneLavadero(false)
                        .ultimaLecturaAgua(2112.26)
                        .ultimaLecturaLuz(0.0)
                        .ultimaLecturaLavadero(0.0)
                        .build(),

                Departamento.builder()
                        .codigo("201").nombre("2° Piso")
                        .propietario("Inquilino 201")
                        .piso(2)
                        .tieneLuz(false)
                        .tieneLavadero(false)
                        .ultimaLecturaAgua(506.93)
                        .ultimaLecturaLuz(0.0)
                        .ultimaLecturaLavadero(0.0)
                        .build(),

                Departamento.builder()
                        .codigo("301").nombre("3° Piso")
                        .propietario("Inquilino 301")
                        .piso(3)
                        .tieneLuz(false)
                        .tieneLavadero(false)
                        .ultimaLecturaAgua(278.69)
                        .ultimaLecturaLuz(0.0)
                        .ultimaLecturaLavadero(0.0)
                        .build(),

                Departamento.builder()
                        .codigo("401").nombre("4° Piso")
                        .propietario("Inquilino 401")
                        .piso(4)
                        .tieneLuz(false)
                        .tieneLavadero(false)
                        .ultimaLecturaAgua(1495.28)
                        .ultimaLecturaLuz(0.0)
                        .ultimaLecturaLavadero(0.0)
                        .build(),

                // Pisos 5-6 (con medidor de luz, algunos con lavadero)
                // 5° Piso A – Nohelia (tiene lavadero)
                Departamento.builder()
                        .codigo("501").nombre("5° Piso A – Nohelia")
                        .propietario("Nohelia")
                        .piso(5)
                        .tieneLuz(true)
                        .tieneLavadero(true)  // ← Tiene lavadero externo
                        .ultimaLecturaAgua(115.077)
                        .ultimaLecturaLuz(1055.1)
                        .ultimaLecturaLavadero(0.0)
                        .build(),

                // 5° Piso B – Cinthya (sin lavadero)
                Departamento.builder()
                        .codigo("502").nombre("5° Piso B – Cinthya")
                        .propietario("Cinthya")
                        .piso(5)
                        .tieneLuz(true)
                        .tieneLavadero(false)
                        .ultimaLecturaAgua(227.59)
                        .ultimaLecturaLuz(2171.0)
                        .ultimaLecturaLavadero(0.0)
                        .build(),

                // 6° Piso A – Julio (tiene lavadero)
                Departamento.builder()
                        .codigo("601").nombre("6° Piso A – Julio")
                        .propietario("Julio")
                        .piso(6)
                        .tieneLuz(true)
                        .tieneLavadero(true)  // ← Tiene lavadero externo
                        .ultimaLecturaAgua(195.65)
                        .ultimaLecturaLuz(1113.9)
                        .ultimaLecturaLavadero(0.0)
                        .build(),

                // 6° Piso B – Milagros (sin lavadero)
                Departamento.builder()
                        .codigo("602").nombre("6° Piso B – Milagros")
                        .propietario("Milagros")
                        .piso(6)
                        .tieneLuz(true)
                        .tieneLavadero(false)
                        .ultimaLecturaAgua(116.88)
                        .ultimaLecturaLuz(7974.7)
                        .ultimaLecturaLavadero(0.0)
                        .build()
        );

        dptoRepo.saveAll(dptos);
        log.info("✅ {} departamentos inicializados", dptos.size());

        // Mostrar resumen de departamentos creados
        dptos.forEach(d -> log.info("  - {} | {} | Piso: {} | Luz: {} | Lavadero: {}",
                d.getCodigo(), d.getNombre(), d.getPiso(),
                d.getTieneLuz() ? "Sí" : "No",
                d.getTieneLavadero() ? "Sí" : "No"));
    }
}