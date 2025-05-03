package com.example.usuarios.service;

import com.example.usuarios.dto.UsuarioRequest;
import com.example.usuarios.dto.UsuarioResponse;
import com.example.usuarios.exception.ResourceNotFoundException;
import com.example.usuarios.exception.UserAlreadyExistsException;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioRequest usuarioRequest;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id("1")
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan.perez@example.com")
                .password("password")
                .roles(Arrays.asList("ROLE_ADMIN"))
                .tipo(Usuario.TipoUsuario.ADMINISTRADOR)
                .build();

        usuarioRequest = new UsuarioRequest();
        usuarioRequest.setNombre("Juan");
        usuarioRequest.setApellido("Pérez");
        usuarioRequest.setEmail("juan.perez@example.com");
        usuarioRequest.setPassword("password");
        usuarioRequest.setRoles(Arrays.asList("ROLE_ADMIN"));
        usuarioRequest.setTipo("ADMINISTRADOR");
    }

    @Test
    void deberiaCrearUsuarioExitosamente() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponse result = usuarioService.crearUsuario(usuarioRequest);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Juan", result.getNombre());
        assertEquals("Pérez", result.getApellido());
        assertEquals("juan.perez@example.com", result.getEmail());
        assertEquals(Arrays.asList("ROLE_ADMIN"), result.getRoles());
        assertEquals(Usuario.TipoUsuario.ADMINISTRADOR, result.getTipo());
    }

    @Test
    void deberiaLanzarExcepcionCuandoEmailYaExiste() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> {
            usuarioService.crearUsuario(usuarioRequest);
        });
    }

    @Test
    void deberiaObtenerUsuarioPorId() {
        when(usuarioRepository.findById(anyString())).thenReturn(Optional.of(usuario));

        UsuarioResponse result = usuarioService.obtenerPorId("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Juan", result.getNombre());
    }

    @Test
    void deberiaLanzarExcepcionCuandoUsuarioNoExiste() {
        when(usuarioRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            usuarioService.obtenerPorId("1");
        });
    }
}