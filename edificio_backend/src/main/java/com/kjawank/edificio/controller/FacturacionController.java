package com.kjawank.edificio.controller;

import com.kjawank.edificio.dto.request.CerrarMesRequest;
import com.kjawank.edificio.dto.request.IngresarDatosRequest;
import com.kjawank.edificio.dto.response.CobroResponse;
import com.kjawank.edificio.dto.response.HistorialMesResponse;
import com.kjawank.edificio.dto.response.ResumenMesResponse;
import com.kjawank.edificio.service.impl.FacturacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/facturacion")
@RequiredArgsConstructor
public class FacturacionController {

    private final FacturacionService facturacionService;

    // ── GET /api/facturacion/mes-actual ───────────────────────────────────────
    // Admin: ve todo | Inquilino: solo su dpto (filtrado aquí)
    @GetMapping("/mes-actual")
    public ResponseEntity<ResumenMesResponse> getMesActual(Authentication auth) {
        ResumenMesResponse resumen = facturacionService.getMesActual();

        // Si es inquilino, filtrar solo su cobro
        if (esInquilino(auth)) {
            String codigoDpto = (String) auth.getDetails();
            resumen.getCobros().removeIf(c -> !c.getCodigoDpto().equals(codigoDpto));
        }
        return ResponseEntity.ok(resumen);
    }

    // ── GET /api/facturacion/historial ────────────────────────────────────────
    @GetMapping("/historial")
    public ResponseEntity<List<HistorialMesResponse>> getHistorial() {
        return ResponseEntity.ok(facturacionService.getHistorial());
    }

    // ── GET /api/facturacion/{periodo} ────────────────────────────────────────
    @GetMapping("/{periodo}")
    public ResponseEntity<ResumenMesResponse> getMesByPeriodo(
            @PathVariable String periodo, Authentication auth) {
        ResumenMesResponse resumen = facturacionService.getMesByPeriodo(periodo);
        if (esInquilino(auth)) {
            String codigoDpto = (String) auth.getDetails();
            resumen.getCobros().removeIf(c -> !c.getCodigoDpto().equals(codigoDpto));
        }
        return ResponseEntity.ok(resumen);
    }

    // ── POST /api/facturacion/ingresar  (solo ADMIN) ──────────────────────────
    @PostMapping("/ingresar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResumenMesResponse> ingresarDatos(
            @Valid @RequestBody IngresarDatosRequest req) {
        return ResponseEntity.ok(facturacionService.ingresarDatos(req));
    }

    // ── POST /api/facturacion/cerrar-mes  (solo ADMIN) ────────────────────────
    @PostMapping("/cerrar-mes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResumenMesResponse> cerrarMes(
            @Valid @RequestBody CerrarMesRequest req) {
        return ResponseEntity.ok(facturacionService.cerrarMes(req));
    }

    // ── POST /api/facturacion/abrir-mes  (solo ADMIN) ─────────────────────────
    @PostMapping("/abrir-mes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResumenMesResponse> abrirMes(
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
                facturacionService.abrirMes(body.get("periodo"), body.get("nombreMes"))
        );
    }

    // ── GET /api/facturacion/dpto/{codigo}/historial ──────────────────────────
    @GetMapping("/dpto/{codigo}/historial")
    public ResponseEntity<List<CobroResponse>> getHistorialDepartamento(
            @PathVariable String codigo, Authentication auth) {
        // Inquilino solo puede ver su propio historial
        if (esInquilino(auth)) {
            String codigoDpto = (String) auth.getDetails();
            if (!codigoDpto.equals(codigo)) {
                return ResponseEntity.status(403).build();
            }
        }
        return ResponseEntity.ok(facturacionService.getHistorialDepartamento(codigo));
    }

    private boolean esInquilino(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_INQUILINO"));
    }
}