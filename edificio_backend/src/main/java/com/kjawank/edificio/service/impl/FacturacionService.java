package com.kjawank.edificio.service.impl;

import com.kjawank.edificio.dto.request.CerrarMesRequest;
import com.kjawank.edificio.dto.request.IngresarDatosRequest;
import com.kjawank.edificio.dto.response.CobroResponse;
import com.kjawank.edificio.dto.response.HistorialMesResponse;
import com.kjawank.edificio.dto.response.ResumenMesResponse;
import com.kjawank.edificio.entity.Departamento;
import com.kjawank.edificio.entity.LecturaConsumo;
import com.kjawank.edificio.entity.MesFacturacion;
import com.kjawank.edificio.entity.MesFacturacion.EstadoMes;
import com.kjawank.edificio.repository.DepartamentoRepository;
import com.kjawank.edificio.repository.LecturaConsumoRepository;
import com.kjawank.edificio.repository.MesFacturacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacturacionService {

    private final MesFacturacionRepository mesRepo;
    private final LecturaConsumoRepository lecturaRepo;
    private final DepartamentoRepository dptoRepo;

    @Value("${app.edificio.area-comun-luz:22.00}")
    private Double areaComunLuz;

    @Value("${app.edificio.total-departamentos:8}")
    private Integer totalDepartamentos;

    public ResumenMesResponse getMesActual() {
        MesFacturacion mes = mesRepo.findByEstado(EstadoMes.ABIERTO)
                .orElseThrow(() -> new RuntimeException("No hay un mes activo. Abre un nuevo mes primero."));
        List<LecturaConsumo> lecturas = lecturaRepo.findByMes(mes);
        return buildResumen(mes, lecturas);
    }

    @Transactional
    public ResumenMesResponse abrirMes(String periodo, String nombreMes) {
        if (mesRepo.existsByPeriodo(periodo)) {
            throw new RuntimeException("El periodo " + periodo + " ya existe.");
        }
        if (mesRepo.findByEstado(EstadoMes.ABIERTO).isPresent()) {
            throw new RuntimeException("Ya hay un mes abierto. Ciérralo antes de abrir uno nuevo.");
        }
        MesFacturacion mes = MesFacturacion.builder()
                .periodo(periodo)
                .nombreMes(nombreMes)
                .sedapalM3(0.0).sedapalImporte(0.0)
                .luzKwh(0.0).luzImporte(0.0)
                .areaComun(areaComunLuz)
                .areaComunPorDpto(round(areaComunLuz / totalDepartamentos))
                .estado(EstadoMes.ABIERTO)
                .build();
        mesRepo.save(mes);
        log.info("Mes abierto: {}", nombreMes);
        return buildResumen(mes, List.of());
    }

    @Transactional
    @Transactional
    public ResumenMesResponse ingresarDatos(IngresarDatosRequest req) {
        MesFacturacion mes = mesRepo.findByPeriodo(req.getPeriodo())
                .orElseThrow(() -> new RuntimeException("Periodo no encontrado: " + req.getPeriodo()));

        if (mes.getEstado() == EstadoMes.CERRADO) {
            throw new RuntimeException("El mes " + mes.getNombreMes() + " ya está cerrado.");
        }

        mes.setSedapalM3(req.getSedapalM3());
        mes.setSedapalImporte(req.getSedapalImporte());
        mes.setLuzKwh(req.getLuzKwh());
        mes.setLuzImporte(req.getLuzImporte());
        mes.setLuzCostoPorKwh(round(req.getLuzImporte() / req.getLuzKwh()));
        mes.setAreaComun(areaComunLuz);
        mes.setAreaComunPorDpto(round(areaComunLuz / totalDepartamentos));
        mesRepo.save(mes);

        List<Departamento> dptos = dptoRepo.findAll();

        // ============================================================
        // PASO 1: Calcular el consumo total de agua (incluyendo lavaderos)
        // ============================================================
        double totalConsumoAguaInterno = 0.0;
        for (Departamento d : dptos) {
            double consumoDepto = round(req.getLecturasAgua().get(d.getCodigo()) - d.getUltimaLecturaAgua());
            double consumoTotalPorDpto = consumoDepto;

            // Consumo del lavadero (solo si tiene y si se envió la lectura)
            if (d.getTieneLavadero() != null && d.getTieneLavadero()
                    && req.getLecturasLavadero() != null
                    && req.getLecturasLavadero().containsKey(d.getCodigo())) {
                Double lectLavaderoActual = req.getLecturasLavadero().get(d.getCodigo());
                double consumoLavadero = round(lectLavaderoActual - d.getUltimaLecturaLavadero());
                consumoTotalPorDpto += consumoLavadero;
            }

            totalConsumoAguaInterno += consumoTotalPorDpto;
        }

        // ============================================================
        // PASO 2: Calcular el consumo total de luz
        // ============================================================
        List<Departamento> dptosConLuz = dptos.stream()
                .filter(d -> d.getTieneLuz() != null && d.getTieneLuz())
                .collect(Collectors.toList());

        double totalConsumoLuzInterno = 0.0;
        for (Departamento d : dptosConLuz) {
            Double lectActual = req.getLecturasLuz().get(d.getCodigo());
            if (lectActual == null) {
                throw new RuntimeException("Falta lectura de luz para el depto: " + d.getCodigo());
            }
            double consumo = round(lectActual - d.getUltimaLecturaLuz());
            if (consumo < 0) {
                throw new RuntimeException("Lectura de luz invalida para " + d.getCodigo());
            }
            totalConsumoLuzInterno += consumo;
        }

        double luzDistribuible = req.getLuzImporte() - areaComunLuz;
        double costoAguaPorM3 = req.getSedapalImporte() / req.getSedapalM3();

        // ============================================================
        // PASO 3: Procesar cada departamento
        // ============================================================
        for (Departamento d : dptos) {
            // ---------- Agua ----------
            double lectAguaActual = req.getLecturasAgua().get(d.getCodigo());
            double consumoDepto = round(lectAguaActual - d.getUltimaLecturaAgua());
            double consumoTotalAguaBruto = consumoDepto;
            // Consumo del lavadero (manejo seguro de nulls)
            Double consumoLavadero = null;
            Double lectLavaderoActual = null;

            if (d.getTieneLavadero() != null && d.getTieneLavadero()) {
                // Verificar que el mapa de lecturas de lavadero no sea null
                if (req.getLecturasLavadero() != null) {
                    lectLavaderoActual = req.getLecturasLavadero().get(d.getCodigo());
                    if (lectLavaderoActual != null) {
                        consumoLavadero = round(lectLavaderoActual - d.getUltimaLecturaLavadero());
                        consumoTotalAguaBruto += consumoLavadero;
                    } else {
                        log.warn("No se encontró lectura de lavadero para el depto: {}", d.getCodigo());
                    }
                } else {
                    log.warn("El mapa de lecturas de lavadero es null para el depto: {}", d.getCodigo());
                }
            }

            double pctAgua = totalConsumoAguaInterno > 0
                    ? round(consumoTotalAguaBruto / totalConsumoAguaInterno)
                    : 0.0;
            double m3Ajustado = round(pctAgua * req.getSedapalM3());
            double pagoAgua = round(m3Ajustado * costoAguaPorM3);

            // ---------- Luz ----------
            double consumoLuz = 0.0;
            double pctLuz = 0.0;
            double pagoLuz = 0.0;
            Double lectLuzActual = null;

            if (d.getTieneLuz() != null && d.getTieneLuz()) {
                lectLuzActual = req.getLecturasLuz().get(d.getCodigo());
                consumoLuz = round(lectLuzActual - d.getUltimaLecturaLuz());
                pctLuz = totalConsumoLuzInterno > 0
                        ? round(consumoLuz / totalConsumoLuzInterno)
                        : 0.0;
                pagoLuz = round(pctLuz * luzDistribuible);
            }

            // ---------- Totales ----------
            double pagoAreaComun = round(areaComunLuz / totalDepartamentos);
            double pagoTotal = round(pagoAgua + pagoLuz + pagoAreaComun);

            // ---------- Guardar ----------
            LecturaConsumo lectura = lecturaRepo
                    .findByMesAndDepartamento(mes, d)
                    .orElse(LecturaConsumo.builder().mes(mes).departamento(d).build());

            lectura.setLecturaAguaActual(lectAguaActual);
            lectura.setConsumoAguaM3(consumoDepto);
            lectura.setPorcentajeAgua(pctAgua);
            lectura.setPagoAgua(pagoAgua);

            if (consumoLavadero != null) {
                lectura.setLecturaLavaderoActual(lectLavaderoActual);
                lectura.setConsumoLavaderoM3(consumoLavadero);
            }

            if (d.getTieneLuz() != null && d.getTieneLuz()) {
                lectura.setLecturaLuzActual(lectLuzActual);
                lectura.setConsumoLuzKwh(consumoLuz);
                lectura.setPorcentajeLuz(pctLuz);
                lectura.setPagoLuz(pagoLuz);
            }

            lectura.setPagoAreaComun(pagoAreaComun);
            lectura.setPagoTotal(pagoTotal);

            lecturaRepo.save(lectura);
        }

        log.info("Datos ingresados para el mes: {}", mes.getNombreMes());
        return buildResumen(mes, lecturaRepo.findByMes(mes));
    }

    @Transactional
    public ResumenMesResponse cerrarMes(CerrarMesRequest req) {
        MesFacturacion mes = mesRepo.findByPeriodo(req.getPeriodoActual())
                .orElseThrow(() -> new RuntimeException("Periodo no encontrado: " + req.getPeriodoActual()));

        if (mes.getEstado() == EstadoMes.CERRADO) {
            throw new RuntimeException("El mes " + mes.getNombreMes() + " ya está cerrado.");
        }

        List<LecturaConsumo> lecturas = lecturaRepo.findByMes(mes);
        if (lecturas.isEmpty()) {
            throw new RuntimeException("No se pueden cerrar el mes sin lecturas ingresadas.");
        }

        for (LecturaConsumo l : lecturas) {
            Departamento d = l.getDepartamento();

            // Actualizar última lectura de agua
            d.setUltimaLecturaAgua(l.getLecturaAguaActual());

            // Actualizar última lectura de lavadero (si tiene)
            if (d.getTieneLavadero() != null && d.getTieneLavadero() && l.getLecturaLavaderoActual() != null) {
                d.setUltimaLecturaLavadero(l.getLecturaLavaderoActual());
            }

            // Actualizar última lectura de luz (si tiene)
            if (d.getTieneLuz() != null && d.getTieneLuz() && l.getLecturaLuzActual() != null) {
                d.setUltimaLecturaLuz(l.getLecturaLuzActual());
            }

            dptoRepo.save(d);
        }

        mes.setEstado(EstadoMes.CERRADO);
        mes.setFechaCierre(LocalDate.now());
        mesRepo.save(mes);

        abrirMes(req.getProximoPeriodo(), req.getProximoNombre());

        log.info("Mes cerrado: {}. Proximo mes abierto: {}", mes.getNombreMes(), req.getProximoNombre());
        return buildResumen(mes, lecturas);
    }

    public List<HistorialMesResponse> getHistorial() {
        return mesRepo.findAllByOrderByPeriodoDesc().stream()
                .map(m -> HistorialMesResponse.builder()
                        .periodo(m.getPeriodo())
                        .nombreMes(m.getNombreMes())
                        .estado(m.getEstado().name())
                        .totalGeneral(lecturaRepo.findByMes(m).stream()
                                .mapToDouble(l -> l.getPagoTotal() != null ? l.getPagoTotal() : 0)
                                .sum())
                        .fechaCierre(m.getFechaCierre())
                        .build())
                .collect(Collectors.toList());
    }

    public ResumenMesResponse getMesByPeriodo(String periodo) {
        MesFacturacion mes = mesRepo.findByPeriodo(periodo)
                .orElseThrow(() -> new RuntimeException("Periodo no encontrado: " + periodo));
        return buildResumen(mes, lecturaRepo.findByMes(mes));
    }

    public List<CobroResponse> getHistorialDepartamento(String codigoDpto) {
        Departamento d = dptoRepo.findByCodigo(codigoDpto)
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado: " + codigoDpto));
        return lecturaRepo.findHistorialByDepartamento(d.getId()).stream()
                .map(l -> buildCobro(l, l.getMes()))
                .collect(Collectors.toList());
    }

    private ResumenMesResponse buildResumen(MesFacturacion mes, List<LecturaConsumo> lecturas) {
        List<CobroResponse> cobros = lecturas.stream()
                .map(l -> buildCobro(l, mes))
                .collect(Collectors.toList());

        double totalAgua = cobros.stream().mapToDouble(c -> c.getPagoAgua() != null ? c.getPagoAgua() : 0).sum();
        double totalLuz = cobros.stream().mapToDouble(c -> c.getPagoLuz() != null ? c.getPagoLuz() : 0).sum();
        double totalGen = cobros.stream().mapToDouble(c -> c.getPagoTotal() != null ? c.getPagoTotal() : 0).sum();

        return ResumenMesResponse.builder()
                .periodo(mes.getPeriodo())
                .nombreMes(mes.getNombreMes())
                .estado(mes.getEstado().name())
                .sedapalM3(mes.getSedapalM3())
                .sedapalImporte(mes.getSedapalImporte())
                .luzKwh(mes.getLuzKwh())
                .luzImporte(mes.getLuzImporte())
                .luzCostoPorKwh(mes.getLuzCostoPorKwh())
                .areaComunTotal(mes.getAreaComun())
                .areaComunPorDpto(mes.getAreaComunPorDpto())
                .totalRecaudadoAgua(round(totalAgua))
                .totalRecaudadoLuz(round(totalLuz))
                .totalGeneral(round(totalGen))
                .fechaCierre(mes.getFechaCierre())
                .cobros(cobros)
                .build();
    }

    private CobroResponse buildCobro(LecturaConsumo l, MesFacturacion mes) {
        Departamento d = l.getDepartamento();
        String msg = buildMensajeWhatsapp(l, mes);

        return CobroResponse.builder()
                .codigoDpto(d.getCodigo())
                .nombreDpto(d.getNombre())
                .propietario(d.getPropietario())
                .tieneLuz(d.getTieneLuz())
                .lecturaAguaActual(l.getLecturaAguaActual())
                .consumoAguaM3(l.getConsumoAguaM3())
                .porcentajeAgua(l.getPorcentajeAgua())
                .pagoAgua(l.getPagoAgua())
                .lecturaLuzActual(l.getLecturaLuzActual())
                .consumoLuzKwh(l.getConsumoLuzKwh())
                .porcentajeLuz(l.getPorcentajeLuz())
                .pagoLuz(l.getPagoLuz())
                .pagoAreaComun(l.getPagoAreaComun())
                .pagoTotal(l.getPagoTotal())
                .mensajeWhatsapp(msg)
                .build();
    }

    private String buildMensajeWhatsapp(LecturaConsumo l, MesFacturacion mes) {
        Departamento d = l.getDepartamento();
        StringBuilder sb = new StringBuilder();
        sb.append("Buenos dias, el monto de *").append(mes.getNombreMes()).append("* es:\n");
        sb.append(String.format("*Agua:* S/ %.2f\n", l.getPagoAgua()));

        // Mostrar consumo del lavadero si aplica
        if (d.getTieneLavadero() != null && d.getTieneLavadero() && l.getConsumoLavaderoM3() != null) {
            sb.append(String.format("  (incluye lavadero: %.2f m3)\n", l.getConsumoLavaderoM3()));
        }

        if (d.getTieneLuz() && l.getPagoLuz() != null) {
            sb.append(String.format("*Luz:* S/ %.2f\n", l.getPagoLuz()));
        }
        sb.append(String.format("*Area comun:* S/ %.2f\n", l.getPagoAreaComun()));
        sb.append("-----------------\n");
        sb.append(String.format("*TOTAL: S/ %.2f*\n", l.getPagoTotal()));
        sb.append("\nConsumo: ");
        sb.append(String.format("%.3f m3 agua", l.getConsumoAguaM3()));
        if (d.getTieneLavadero() != null && d.getTieneLavadero() && l.getConsumoLavaderoM3() != null) {
            sb.append(String.format(" + %.3f m3 lavadero", l.getConsumoLavaderoM3()));
        }
        if (d.getTieneLuz() && l.getConsumoLuzKwh() != null) {
            sb.append(String.format(" | %.2f kWh luz", l.getConsumoLuzKwh()));
        }
        sb.append("\nGracias");
        return sb.toString();
    }

    private double round(double value) {
        return new BigDecimal(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}