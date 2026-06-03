package com.kjawank.edificio.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String username;
    private String rol;
    private String codigoDpto;   // solo si es INQUILINO
    private String nombreDpto;
}