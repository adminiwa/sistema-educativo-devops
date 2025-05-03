package com.example.asignaturas.service;

import com.example.asignaturas.dto.AsignaturaRequest;
import com.example.asignaturas.dto.AsignaturaResponse;
import com.example.asignaturas.exception.AsignaturaAlreadyExistsException;
import com.example.asignaturas.exception.ResourceNotFoundException;
import com.example.asignaturas.model.Asignatura;
import com.example.asignaturas.repository.AsignaturaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AsignaturaServiceTest {

    @Mock
    private AsignaturaRepository asignaturaRepository;

    @InjectMocks
    private AsignaturaService asignaturaService;

    private Asignatura asignatura;
    private AsignaturaRequest asignaturaRequest;

    @BeforeEach
    void setUp() {
        asignatura = Asignatura.builder()
                .id("1")
                .codigo("MAT101")
                .nombre("Matemáticas Básicas")
                .descripcion("Curso introductorio de matemáticas")
                .creditos(4)
                .profesorId("prof123")
                .nivel(Asignatura.Nivel.BASICO)
                .activa(true)
                .build();

        asignaturaRequest = new AsignaturaRequest();
        asignaturaRequest.setCodigo("MAT101");
        asignaturaRequest.setNombre("Matemáticas Básicas");
        asignaturaRequest.setDescripcion("Curso introductorio de matemáticas");
        asignaturaRequest.setCreditos(4);
        asignaturaRequest.setProfesorId("prof123");
        asignaturaRequest.setNivel("BASICO");
        asignaturaRequest.setActiva(true);
    }

    @Test
    void deberiaCrearAsignaturaExitosamente() {
        when(asignaturaRepository.existsByCodigo(anyString())).thenReturn(false);
        when(asignaturaRepository.save(any(Asignatura.class))).thenReturn(asignatura);

        AsignaturaResponse result = asignaturaService.crearAsignatura(asignaturaRequest);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("MAT101", result.getCodigo());
        assertEquals("Matemáticas Básicas", result.getNombre());
        assertEquals(4, result.getCreditos());
        assertEquals(Asignatura.Nivel.BASICO, result.getNivel());
    }

    @Test
    void deberiaLanzarExcepcionCuandoCodigoYaExiste() {
        when(asignaturaRepository.existsByCodigo(anyString())).thenReturn(true);

        assertThrows(AsignaturaAlreadyExistsException.class, () -> {
            asignaturaService.crearAsignatura(asignaturaRequest);
        });
    }

    @Test
    void deberiaObtenerAsignaturaPorId() {
        when(asignaturaRepository.findById(anyString())).thenReturn(Optional.of(asignatura));

        AsignaturaResponse result = asignaturaService.obtenerPorId("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("MAT101", result.getCodigo());
    }

    @Test
    void deberiaLanzarExcepcionCuandoAsignaturaNoExiste() {
        when(asignaturaRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            asignaturaService.obtenerPorId("1");
        });
    }
}