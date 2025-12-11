package com.mecamotors.api.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String nombre; // Para crear el Cliente asociado
    private String apellido;
    private String telefono;
    private Set<String> roles; // Ej: ["admin", "mecanico"] o null (por defecto user)
}