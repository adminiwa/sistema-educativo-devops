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
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatriculaService {
    
    private final MatriculaRepository matriculaRepository;
    private final UsuarioClient usuarioClient;
    private final AsignaturaClient asignaturaClient;
    
    public MatriculaResponse crearMatricula(MatriculaRequest request) {
        // Verificar si ya existe una matrícula para ese estudiante, asignatura y periodo
        if (matriculaRepository.existsByEstudianteIdAndAsignaturaIdAndPeriodoAcademico(
                request.getEstudianteId(), request.getAsignaturaId(), request.getPeriodoAcademico())) {
            throw new MatriculaAlreadyExistsException("Ya existe una matrícula para este estudiante en esta asignatura para el periodo: " + request.getPeriodoAcademico());
        }
        
        // Verificar que el estudiante existe
        UsuarioDto estudiante = getUsuario(request.getEstudianteId());
        
        // Verificar que la asignatura existe
        AsignaturaDto asignatura = getAsignatura(request.getAsignaturaId());
        
        Matricula matricula = Matricula.builder()
                .estudianteId(request.getEstudianteId())
                .asignaturaId(request.getAsignaturaId())
                .fechaMatricula(LocalDateTime.now())
                .estado(Matricula.EstadoMatricula.INSCRITA)
                .periodoAcademico(request.getPeriodoAcademico())
                .build();
        
        Matricula guardada = matriculaRepository.save(matricula);
        
        return mapToDto(guardada, estudiante, asignatura);
    }
    
    public List<MatriculaResponse> obtenerTodas() {
        return matriculaRepository.findAll().stream()
                .map(this::enrichMatricula)
                .collect(Collectors.toList());
    }
    
    public MatriculaResponse obtenerPorId(String id) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula no encontrada con id: " + id));
        
        return enrichMatricula(matricula);
    }
    
    public List<MatriculaResponse> obtenerPorEstudiante(String estudianteId) {
        return matriculaRepository.findByEstudianteId(estudianteId).stream()
                .map(this::enrichMatricula)
                .collect(Collectors.toList());
    }
    
    public List<MatriculaResponse> obtenerPorAsignatura(String asignaturaId) {
        return matriculaRepository.findByAsignaturaId(asignaturaId).stream()
                .map(this::enrichMatricula)
                .collect(Collectors.toList());
    }
    
    public List<MatriculaResponse> obtenerPorPeriodo(String periodoAcademico) {
        return matriculaRepository.findByPeriodoAcademico(periodoAcademico).stream()
                .map(this::enrichMatricula)
                .collect(Collectors.toList());
    }
    
    public MatriculaResponse actualizarEstado(String id, Matricula.EstadoMatricula nuevoEstado) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula no encontrada con id: " + id));
        
        matricula.setEstado(nuevoEstado);
        Matricula actualizada = matriculaRepository.save(matricula);
        
        return enrichMatricula(actualizada);
    }
    
    public MatriculaResponse actualizarCalificacion(String id, Double calificacion) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula no encontrada con id: " + id));
        
        matricula.setCalificacion(calificacion);
        if (calificacion != null) {
            matricula.setEstado(Matricula.EstadoMatricula.COMPLETADA);
        }
        
        Matricula actualizada = matriculaRepository.save(matricula);
        
        return enrichMatricula(actualizada);
    }
    
    public void eliminarMatricula(String id) {
        if (!matriculaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Matrícula no encontrada con id: " + id);
        }
        
        matriculaRepository.deleteById(id);
    }
    
    private MatriculaResponse enrichMatricula(Matricula matricula) {
        UsuarioDto estudiante = null;
        AsignaturaDto asignatura = null;
        
        try {
            estudiante = getUsuario(matricula.getEstudianteId());
        } catch (Exception e) {
            log.error("Error al obtener información del estudiante: {}", e.getMessage());
        }
        
        try {
            asignatura = getAsignatura(matricula.getAsignaturaId());
        } catch (Exception e) {
            log.error("Error al obtener información de la asignatura: {}", e.getMessage());
        }
        
        return mapToDto(matricula, estudiante, asignatura);
    }
    
    @CircuitBreaker(name = "usuarioService", fallbackMethod = "getUsuarioFallback")
    private UsuarioDto getUsuario(String id) {
        return usuarioClient.getUsuarioById(id);
    }
    
    private UsuarioDto getUsuarioFallback(String id, Exception e) {
        log.error("Fallback: Error al obtener usuario {}: {}", id, e.getMessage());
        return UsuarioDto.builder()
                .id(id)
                .nombre("Usuario no disponible")
                .apellido("")
                .email("")
                .build();
    }
    
    @CircuitBreaker(name = "asignaturaService", fallbackMethod = "getAsignaturaFallback")
    private AsignaturaDto getAsignatura(String id) {
        return asignaturaClient.getAsignaturaById(id);
    }
    
    private AsignaturaDto getAsignaturaFallback(String id, Exception e) {
        log.error("Fallback: Error al obtener asignatura {}: {}", id, e.getMessage());
        return AsignaturaDto.builder()
                .id(id)
                .codigo("")
                .nombre("Asignatura no disponible")
                .creditos(0)
                .build();
    }
    
    private MatriculaResponse mapToDto(Matricula matricula, UsuarioDto estudiante, AsignaturaDto asignatura) {
        return MatriculaResponse.builder()
                .id(matricula.getId())
                .estudianteId(matricula.getEstudianteId())
                .asignaturaId(matricula.getAsignaturaId())
                .fechaMatricula(matricula.getFechaMatricula())
                .estado(matricula.getEstado())
                .calificacion(matricula.getCalificacion())
                .periodoAcademico(matricula.getPeriodoAcademico())
                .estudiante(estudiante)
                .asignatura(asignatura)
                .build();
    }
}