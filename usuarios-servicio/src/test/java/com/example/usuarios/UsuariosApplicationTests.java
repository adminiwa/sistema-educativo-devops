package com.example.usuarios;

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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UsuariosApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
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
    }

    @Test
    void deberiaAutenticarUsuarioYDevolverToken() {
        AuthRequest request = new AuthRequest("admin@example.com", "password");

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                baseUrl + "/api/auth/login",
                request,
                AuthResponse.class);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody().getToken());
    }

    @Test
    void deberiaCrearNuevoUsuarioYObtenerlo() {
        // Autenticar para obtener token
        AuthRequest authRequest = new AuthRequest("admin@example.com", "password");
        ResponseEntity<AuthResponse> authResponse = restTemplate.postForEntity(
                baseUrl + "/api/auth/login",
                authRequest,
                AuthResponse.class);
        
        String token = authResponse.getBody().getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        // Crear nuevo usuario
        UsuarioRequest usuarioRequest = new UsuarioRequest();
        usuarioRequest.setNombre("Nuevo");
        usuarioRequest.setApellido("Usuario");
        usuarioRequest.setEmail("nuevo@example.com");
        usuarioRequest.setPassword("password");
        usuarioRequest.setRoles(Arrays.asList("ROLE_PROFESOR"));
        usuarioRequest.setTipo("PROFESOR");

        HttpEntity<UsuarioRequest> requestEntity = new HttpEntity<>(usuarioRequest, headers);
        ResponseEntity<UsuarioResponse> createResponse = restTemplate.exchange(
                baseUrl + "/api/usuarios",
                HttpMethod.POST,
                requestEntity,
                UsuarioResponse.class);

        assertEquals(201, createResponse.getStatusCodeValue());
        
        // Obtener el usuario creado
        HttpEntity<Void> getEntity = new HttpEntity<>(headers);
        ResponseEntity<UsuarioResponse> getResponse = restTemplate.exchange(
                baseUrl + "/api/usuarios/" + createResponse.getBody().getId(),
                HttpMethod.GET,
                getEntity,
                UsuarioResponse.class);

        assertEquals(200, getResponse.getStatusCodeValue());
        assertEquals("Nuevo", getResponse.getBody().getNombre());
        assertEquals("Usuario", getResponse.getBody().getApellido());
        assertEquals("nuevo@example.com", getResponse.getBody().getEmail());
    }
}