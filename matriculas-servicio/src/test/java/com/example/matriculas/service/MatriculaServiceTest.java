package com.example.matriculas.service;

import com.example.matriculas.client.AsignaturaClient;
import com.example.matriculas.client.UsuarioClient;
import com.example.matriculas.dto.AsignaturaDto;
import com.example.matriculas.dto.MatriculaRequest;
import com.example.matriculas.dto.MatriculaResponse;
import com.example.matriculas.dto.UsuarioDto;
import com.example.matriculas.exception.MatriculaAlreadyExistsException;
import com.example.matriculas.exception.ResourceNotFoundException;
import com.example.matriculas.model.Matricula;
import com.example.matriculas.repository.MatriculaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatriculaServiceTest {

    @Mock
    private MatriculaRepository matriculaRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private AsignaturaClient asignaturaClient;

    @InjectMocks
    private MatriculaService matriculaService;

    private Matricula matricula;
    private MatriculaRequest matriculaRequest;
    private UsuarioDto usuarioDto;
    private AsignaturaDto asignaturaDto;

    @BeforeEach
    void setUp() {
        matricula = Matricula.builder()
                .id("1")
                .estudianteId("est123")
                .asignaturaId("asig456")
                .fechaMatricula(LocalDateTime.now())
                .estado(Matricula.EstadoMatricula.INSCRITA)
                .periodoAcademico("2023-1")
                .build();

        matriculaRequest = new MatriculaRequest();
        matriculaRequest.setEstudianteId("est123");
        matriculaRequest.setAsignaturaId("asig456");
        matriculaRequest.setPeriodoAcademico("2023-1");

        usuarioDto = UsuarioDto.builder()
                .id("est123")
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan.perez@example.com")
                .build();

        asignaturaDto = AsignaturaDto.builder()
                .id("asig456")
                .codigo("MAT101")
                .nombre("Matemáticas Básicas")
                .creditos(4)
                .profesorId("prof789")
                .build();
    }

    @Test
    void deberiaCrearMatriculaExitosamente() {
        when(matriculaRepository.existsByEstudianteIdAndAsignaturaIdAndPeriodoAcademico(anyString(), anyString(), anyString())).thenReturn(false);
        when(usuarioClient.getUsuarioById(anyString())).thenReturn(usuarioDto);
        when(asignaturaClient.getAsignaturaById(anyString())).thenReturn(asignaturaDto);
        when(matriculaRepository.save(any(Matricula.class))).thenReturn(matricula);

        MatriculaResponse result = matriculaService.crearMatricula(matriculaRequest);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("est123", result.getEstudianteId());
        assertEquals("asig456", result.getAsignaturaId());
        assertEquals(Matricula.EstadoMatricula.INSCRITA, result.getEstado());
        assertEquals("Juan", result.getEstudiante().getNombre());
        assertEquals("Matemáticas Básicas", result.getAsignatura().getNombre());
    }

    @Test
    void deberiaLanzarExcepcionCuandoMatriculaYaExiste() {
        when(matriculaRepository.existsByEstudianteIdAndAsignaturaIdAndPeriodoAcademico(anyString(), anyString(), anyString())).thenReturn(true);

        assertThrows(MatriculaAlreadyExistsException.class, () -> {
            matriculaService.crearMatricula(matriculaRequest);
        });
    }

    @Test
    void deberiaObtenerMatriculaPorId() {
        when(matriculaRepository.findById(anyString())).thenReturn(Optional.of(matricula));
        when(usuarioClient.getUsuarioById(anyString())).thenReturn(usuarioDto);
        when(asignaturaClient.getAsignaturaById(anyString())).thenReturn(asignaturaDto);

        MatriculaResponse result = matriculaService.obtenerPorId("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("est123", result.getEstudianteId());
        assertEquals("Juan", result.getEstudiante().getNombre());
        assertEquals("Matemáticas Básicas", result.getAsignatura().getNombre());
    }

    @Test
    void deberiaLanzarExcepcionCuandoMatriculaNoExiste() {
        when(matriculaRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            matriculaService.obtenerPorId("1");
        });
    }
}