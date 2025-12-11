package com.mecamotors.api.services;

import com.mecamotors.api.dtos.JwtResponse;
import com.mecamotors.api.dtos.LoginRequest;
import com.mecamotors.api.dtos.RegisterRequest;
import com.mecamotors.api.entities.enums.RolNombre;
import com.mecamotors.api.entities.models.Rol;
import com.mecamotors.api.entities.models.Usuario;
import com.mecamotors.api.entities.models.Cliente;
import com.mecamotors.api.repositories.RolRepository;
import com.mecamotors.api.repositories.UsuarioRepository;
import com.mecamotors.api.repositories.ClienteRepository;
// import com.mecamotors.api.security.jwt.JwtUtils; // (Lo crearemos luego)

import com.mecamotors.api.security.jwt.JwtUtils;
import com.mecamotors.api.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // Inyecta automáticamente los final
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ClienteRepository clienteRepository; // Para crear perfil de cliente al registrarse
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Transactional
    public void registrarUsuario(RegisterRequest signUpRequest) {
        // 1. Validaciones previas
        if (usuarioRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Error: ¡El username ya está en uso!");
        }
        if (usuarioRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Error: ¡El email ya está registrado!");
        }

        // 2. Determinar el Rol (Por defecto CLIENTE si no se especifica)
        // Nota: En producción, deberías restringir quién puede crear roles ADMIN
        String strRol = (signUpRequest.getRoles() != null && !signUpRequest.getRoles().isEmpty())
                ? signUpRequest.getRoles().iterator().next()
                : "CLIENTE";

        RolNombre rolNombre = RolNombre.valueOf(strRol.toUpperCase());
        Rol rol = rolRepository.findByNombre(rolNombre)
                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));

        // 3. Crear el Usuario
        Usuario usuario = Usuario.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword())) // ¡Password hasheado!
                .rol(rol)
                .activo(true)
                .build();

        usuarioRepository.save(usuario);

        // 4. Si el rol es CLIENTE, creamos automáticamente su ficha en la tabla 'clientes'
        if (rolNombre == RolNombre.CLIENTE) {
            Cliente cliente = Cliente.builder()
                    .nombre(signUpRequest.getNombre())
                    .apellido(signUpRequest.getApellido())
                    .email(signUpRequest.getEmail())
                    .telefono(signUpRequest.getTelefono())
                    .usuario(usuario) // Vinculamos
                    .build();
            clienteRepository.save(cliente);
        }
    }

    public JwtResponse autenticarUsuario(LoginRequest loginRequest) {
        // 1. Autenticar
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 2. Generar el Token REAL usando JwtUtils
        String jwt = jwtUtils.generateJwtToken(authentication);

        // 3. Obtener detalles
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Si prefieres usar la entidad Usuario para sacar datos extra:
        Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername()).orElseThrow();

        return new JwtResponse(
                jwt,
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getRol().getNombre().toString()
        );
    }
}