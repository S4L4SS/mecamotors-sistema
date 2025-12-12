package com.mecamotors.api.dtos;

import lombok.Data;
import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String username, String email, String rol) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = List.of(rol); // Convertir el rol a lista
    }
}