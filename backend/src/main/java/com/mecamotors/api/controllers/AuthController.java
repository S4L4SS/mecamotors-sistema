package com.mecamotors.api.controllers;

import com.mecamotors.api.dtos.JwtResponse;
import com.mecamotors.api.dtos.LoginRequest;
import com.mecamotors.api.dtos.RegisterRequest;
import com.mecamotors.api.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Permitir peticiones desde Angular/Postman
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegisterRequest request) {
        authService.registrarUsuario(request);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> autenticarUsuario(@RequestBody LoginRequest request) {
        JwtResponse response = authService.autenticarUsuario(request);
        return ResponseEntity.ok(response);
    }
}