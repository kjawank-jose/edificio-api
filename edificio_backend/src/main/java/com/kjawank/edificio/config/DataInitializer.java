package com.kjawank.edificio.config;

import com.kjawank.edificio.entity.Departamento;
import com.kjawank.edificio.entity.MesFacturacion;
import com.kjawank.edificio.entity.Usuario;
import com.kjawank.edificio.repository.DepartamentoRepository;
import com.kjawank.edificio.repository.MesFacturacionRepository;
import com.kjawank.edificio.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final DepartamentoRepository dptoRepo;
    private final UsuarioRepository usuarioRepo;
    private final MesFacturacionRepository mesRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (dptoRepo.count() == 0) {
            initDepartamentos();
        }
        if (usuarioRepo.count() == 0) {
            initUsuarios();
        }
        if (mesRepo.count() == 0) {
            initMesActual();
        }
    }

    private void initDepartamentos() {
        // Lecturas iniciales extraídas del historial (cierre de Abril 2026)
        List<Departamento> dptos = List.of(
                Departamento.builder().codigo("101").nombre("1° Piso")            .propietario("Inquilino 101").piso(1).tieneLuz(false).ultimaLecturaAgua(2112.26).build(),
                Departamento.builder().codigo("201").nombre("2° Piso")            .propietario("Inquilino 201").piso(2).tieneLuz(false).ultimaLecturaAgua(506.93) .build(),
                Departamento.builder().codigo("301").nombre("3° Piso")            .propietario("Inquilino 301").piso(3).tieneLuz(false).ultimaLecturaAgua(278.69) .build(),
                Departamento.builder().codigo("401").nombre("4° Piso")            .propietario("Inquilino 401").piso(4).tieneLuz(false).ultimaLecturaAgua(1495.28).build(),
                Departamento.builder().codigo("501").nombre("5° Piso A – Nohelia").propietario("Nohelia")      .piso(5).tieneLuz(true) .ultimaLecturaAgua(115.077).ultimaLecturaLuz(1055.1).build(),
                Departamento.builder().codigo("502").nombre("5° Piso B – Cinthya").propietario("Cinthya")      .piso(5).tieneLuz(true) .ultimaLecturaAgua(227.59) .ultimaLecturaLuz(2171.0).build(),
                Departamento.builder().codigo("601").nombre("6° Piso A – Julio")  .propietario("Julio")        .piso(6).tieneLuz(true) .ultimaLecturaAgua(195.65) .ultimaLecturaLuz(1113.9).build(),
                Departamento.builder().codigo("602").nombre("6° Piso B – Milagros").propietario("Milagros")    .piso(6).tieneLuz(true) .ultimaLecturaAgua(116.88) .ultimaLecturaLuz(7974.7).build()
        );
        dptoRepo.saveAll(dptos);
        log.info("✅ {} departamentos inicializados", dptos.size());
    }

    private void initUsuarios() {
        // Admin
        usuarioRepo.save(Usuario.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .rol(Usuario.Rol.ADMIN)
                .build());

        // Un usuario por departamento (contraseña = codigo del dpto)
        dptoRepo.findAll().forEach(d ->
                usuarioRepo.save(Usuario.builder()
                        .username("dpto" + d.getCodigo())
                        .password(passwordEncoder.encode(d.getCodigo()))
                        .rol(Usuario.Rol.INQUILINO)
                        .departamento(d)
                        .build())
        );
        log.info("✅ Usuarios inicializados. Admin: admin/admin123 | Inquilinos: dptoXXX/XXX");
    }

    private void initMesActual() {
        // Abre Mayo 2026 como primer mes activo
        MesFacturacion mes = MesFacturacion.builder()
                .periodo("2026-05")
                .nombreMes("Mayo 2026")
                .sedapalM3(0.0).sedapalImporte(0.0)
                .luzKwh(0.0).luzImporte(0.0)
                .areaComun(22.0)
                .areaComunPorDpto(2.75)
                .estado(MesFacturacion.EstadoMes.ABIERTO)
                .build();
        mesRepo.save(mes);
        log.info("✅ Mes activo inicializado: Mayo 2026");
    }
}