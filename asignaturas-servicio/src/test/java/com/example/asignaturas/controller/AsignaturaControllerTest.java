package com.example.asignaturas.controller;

import com.example.asignaturas.dto.AsignaturaRequest;
import com.example.asignaturas.dto.AsignaturaResponse;
import com.example.asignaturas.model.Asignatura;
import com.example.asignaturas.service.AsignaturaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AsignaturaController.class)
public class AsignaturaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AsignaturaService asignaturaService;

    @Autowired
    private ObjectMapper objectMapper;

    private AsignaturaRequest asignaturaRequest;
    private AsignaturaResponse asignaturaResponse;

    @BeforeEach
    void setUp() {
        asignaturaRequest = new AsignaturaRequest();
        asignaturaRequest.setCodigo("MAT101");
        asignaturaRequest.setNombre("Matemáticas Básicas");
        asignaturaRequest.setDescripcion("Curso introductorio de matemáticas");
        asignaturaRequest.setCreditos(4);
        asignaturaRequest.setProfesorId("prof123");
        asignaturaRequest.setNivel("BASICO");
        asignaturaRequest.setActiva(true);

        asignaturaResponse = AsignaturaResponse.builder()
                .id("1")
                .codigo("MAT101")
                .nombre("Matemáticas Básicas")
                .descripcion("Curso introductorio de matemáticas")
                .creditos(4)
                .profesorId("prof123")
                .nivel(Asignatura.Nivel.BASICO)
                .activa(true)
                .build();
    }

    @Test
    void deberiaObtenerTodasLasAsignaturas() throws Exception {
        List<AsignaturaResponse> asignaturas = Arrays.asList(asignaturaResponse);
        when(asignaturaService.obtenerTodas()).thenReturn(asignaturas);

        mockMvc.perform(get("/api/asignaturas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].codigo", is("MAT101")))
                .andExpect(jsonPath("$[0].nombre", is("Matemáticas Básicas")));
    }

    @Test
    void deberiaObtenerAsignaturaPorId() throws Exception {
        when(asignaturaService.obtenerPorId(anyString())).thenReturn(asignaturaResponse);

        mockMvc.perform(get("/api/asignaturas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.codigo", is("MAT101")));
    }

    @Test
    void deberiaCrearAsignatura() throws Exception {
        when(asignaturaService.crearAsignatura(any(AsignaturaRequest.class))).thenReturn(asignaturaResponse);

        mockMvc.perform(post("/api/asignaturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(asignaturaRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.codigo", is("MAT101")));
    }

    @Test
    void deberiaActualizarAsignatura() throws Exception {
        when(asignaturaService.actualizarAsignatura(anyString(), any(AsignaturaRequest.class))).thenReturn(asignaturaResponse);

        mockMvc.perform(put("/api/asignaturas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(asignaturaRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.codigo", is("MAT101")));
    }

    @Test
    void deberiaEliminarAsignatura() throws Exception {
        mockMvc.perform(delete("/api/asignaturas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}