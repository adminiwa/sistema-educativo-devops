package com.example.usuarios.integration;

import com.example.usuarios.dto.AuthRequest;
import com.example.usuarios.dto.AuthResponse;
import com.example.usuarios.dto.UsuarioRequest;
import com.example.usuarios.dto.UsuarioResponse;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private WebTestClient webTestClient;
    private String adminToken;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        usuarioRepository.deleteAll();

        // Crear usuario administrador para pruebas
        Usuario admin = Usuario.builder()
                .nombre("Admin")
                .apellido("Test")
                .email("admin@example.com")
                .password(passwordEncoder.encode("password"))
                .roles(Arrays.asList("ROLE_ADMIN"))
                .tipo(Usuario.TipoUsuario.ADMINISTRADOR)
                .build();

        usuarioRepository.save(admin);

        // Autenticar y obtener token
        AuthRequest authRequest = new AuthRequest("admin@example.com", "password");
        AuthResponse authResponse = webTestClient.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(authRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponse.class)
                .returnResult()
                .getResponseBody();

        adminToken = authResponse.getToken();
    }

    @Test
    void deberiaCrearUsuarioYListarlos() {
        // Crear nuevo usuario
        UsuarioRequest usuarioRequest = new UsuarioRequest();
        usuarioRequest.setNombre("Profesor");
        usuarioRequest.setApellido("Test");
        usuarioRequest.setEmail("profesor@example.com");
        usuarioRequest.setPassword("password");
        usuarioRequest.setRoles(Arrays.asList("ROLE_PROFESOR"));
        usuarioRequest.setTipo("PROFESOR");

        webTestClient.post()
                .uri("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + adminToken)
                .bodyValue(usuarioRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UsuarioResponse.class);

        // Listar todos los usuarios
        webTestClient.get()
                .uri("/api/usuarios")
                .header("Authorization", "Bearer " + adminToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UsuarioResponse.class)
                .hasSize(2);
    }

    @Test
    void deberiaRechazarAccesoSinAutenticacion() {
        webTestClient.get()
                .uri("/api/usuarios")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void deberiaProcesarLoginCorrectamente() {
        AuthRequest authRequest = new AuthRequest("admin@example.com", "password");
        
        webTestClient.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(authRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").isNotEmpty()
                .jsonPath("$.email").isEqualTo("admin@example.com");
    }
}