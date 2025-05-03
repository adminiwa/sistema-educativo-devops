package com.example.usuarios.controller;

import com.example.usuarios.dto.UsuarioRequest;
import com.example.usuarios.dto.UsuarioResponse;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioRequest usuarioRequest;
    private UsuarioResponse usuarioResponse;

    @BeforeEach
    void setUp() {
        usuarioRequest = new UsuarioRequest();
        usuarioRequest.setNombre("Juan");
        usuarioRequest.setApellido("Pérez");
        usuarioRequest.setEmail("juan.perez@example.com");
        usuarioRequest.setPassword("password");
        usuarioRequest.setRoles(Arrays.asList("ROLE_ADMIN"));
        usuarioRequest.setTipo("ADMINISTRADOR");

        usuarioResponse = UsuarioResponse.builder()
                .id("1")
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan.perez@example.com")
                .roles(Arrays.asList("ROLE_ADMIN"))
                .tipo(Usuario.TipoUsuario.ADMINISTRADOR)
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deberiaObtenerTodosLosUsuarios() throws Exception {
        List<UsuarioResponse> usuarios = Arrays.asList(usuarioResponse);
        when(usuarioService.obtenerTodos()).thenReturn(usuarios);

        mockMvc.perform(get("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].nombre", is("Juan")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deberiaObtenerUsuarioPorId() throws Exception {
        when(usuarioService.obtenerPorId(anyString())).thenReturn(usuarioResponse);

        mockMvc.perform(get("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.nombre", is("Juan")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deberiaCrearUsuario() throws Exception {
        when(usuarioService.crearUsuario(any(UsuarioRequest.class))).thenReturn(usuarioResponse);

        mockMvc.perform(post("/api/usuarios")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.nombre", is("Juan")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deberiaActualizarUsuario() throws Exception {
        when(usuarioService.actualizarUsuario(anyString(), any(UsuarioRequest.class))).thenReturn(usuarioResponse);

        mockMvc.perform(put("/api/usuarios/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.nombre", is("Juan")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deberiaEliminarUsuario() throws Exception {
        mockMvc.perform(delete("/api/usuarios/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}