package com.mecamotors.api.config;

import com.mecamotors.api.entities.enums.RolNombre;
import com.mecamotors.api.entities.models.Rol;
import com.mecamotors.api.entities.models.Usuario;
import com.mecamotors.api.repositories.RolRepository;
import com.mecamotors.api.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear roles si no existen
        for (RolNombre rolNombre : RolNombre.values()) {
            if (rolRepository.findByNombre(rolNombre).isEmpty()) {
                Rol rol = Rol.builder()
                        .nombre(rolNombre)
                        .build();
                rolRepository.save(rol);
                log.info("Rol creado: {}", rolNombre);
            }
        }
        log.info("Roles inicializados correctamente");

        // Crear usuario admin por defecto si no existe
        if (!usuarioRepository.existsByUsername("admin123")) {
            Rol rolAdmin = rolRepository.findByNombre(RolNombre.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));
            
            Usuario admin = Usuario.builder()
                    .username("admin123")
                    .email("admin@mecamotors.com")
                    .password(passwordEncoder.encode("admin123"))
                    .rol(rolAdmin)
                    .activo(true)
                    .build();
            
            usuarioRepository.save(admin);
            log.info("Usuario administrador creado: admin123/admin123");
        }
    }
}
