package com.example.asignaturas.service;

import com.example.asignaturas.dto.AsignaturaRequest;
import com.example.asignaturas.dto.AsignaturaResponse;
import com.example.asignaturas.exception.AsignaturaAlreadyExistsException;
import com.example.asignaturas.exception.ResourceNotFoundException;
import com.example.asignaturas.model.Asignatura;
import com.example.asignaturas.repository.AsignaturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsignaturaService {
    
    private final AsignaturaRepository asignaturaRepository;
    
    public AsignaturaResponse crearAsignatura(AsignaturaRequest asignaturaRequest) {
        if (asignaturaRepository.existsByCodigo(asignaturaRequest.getCodigo())) {
            throw new AsignaturaAlreadyExistsException("El código de asignatura ya existe: " + asignaturaRequest.getCodigo());
        }
        
        Asignatura asignatura = Asignatura.builder()
                .codigo(asignaturaRequest.getCodigo())
                .nombre(asignaturaRequest.getNombre())
                .descripcion(asignaturaRequest.getDescripcion())
                .creditos(asignaturaRequest.getCreditos())
                .profesorId(asignaturaRequest.getProfesorId())
                .nivel(Asignatura.Nivel.valueOf(asignaturaRequest.getNivel()))
                .activa(asignaturaRequest.isActiva())
                .build();
        
        Asignatura guardada = asignaturaRepository.save(asignatura);
        
        return mapToDto(guardada);
    }
    
    public List<AsignaturaResponse> obtenerTodas() {
        return asignaturaRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    public AsignaturaResponse obtenerPorId(String id) {
        Asignatura asignatura = asignaturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada con id: " + id));
        
        return mapToDto(asignatura);
    }
    
    public AsignaturaResponse obtenerPorCodigo(String codigo) {
        Asignatura asignatura = asignaturaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada con código: " + codigo));
        
        return mapToDto(asignatura);
    }
    
    public List<AsignaturaResponse> obtenerPorProfesor(String profesorId) {
        return asignaturaRepository.findByProfesorId(profesorId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    public List<AsignaturaResponse> obtenerPorNivel(String nivel) {
        try {
            Asignatura.Nivel nivelEnum = Asignatura.Nivel.valueOf(nivel.toUpperCase());
            return asignaturaRepository.findByNivel(nivelEnum).stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nivel no válido: " + nivel);
        }
    }
    
    public List<AsignaturaResponse> obtenerAsignaturasActivas() {
        return asignaturaRepository.findByActivaTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    public AsignaturaResponse actualizarAsignatura(String id, AsignaturaRequest asignaturaRequest) {
        Asignatura asignatura = asignaturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignatura no encontrada con id: " + id));
        
        // Verificamos si se intenta cambiar el código a uno que ya existe
        if (!asignatura.getCodigo().equals(asignaturaRequest.getCodigo()) && 
                asignaturaRepository.existsByCodigo(asignaturaRequest.getCodigo())) {
            throw new AsignaturaAlreadyExistsException("El código de asignatura ya existe: " + asignaturaRequest.getCodigo());
        }
        
        asignatura.setCodigo(asignaturaRequest.getCodigo());
        asignatura.setNombre(asignaturaRequest.getNombre());
        asignatura.setDescripcion(asignaturaRequest.getDescripcion());
        asignatura.setCreditos(asignaturaRequest.getCreditos());
        asignatura.setProfesorId(asignaturaRequest.getProfesorId());
        asignatura.setNivel(Asignatura.Nivel.valueOf(asignaturaRequest.getNivel()));
        asignatura.setActiva(asignaturaRequest.isActiva());
        
        Asignatura actualizada = asignaturaRepository.save(asignatura);
        
        return mapToDto(actualizada);
    }
    
    public void eliminarAsignatura(String id) {
        if (!asignaturaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Asignatura no encontrada con id: " + id);
        }
        
        asignaturaRepository.deleteById(id);
    }
    
    private AsignaturaResponse mapToDto(Asignatura asignatura) {
        return AsignaturaResponse.builder()
                .id(asignatura.getId())
                .codigo(asignatura.getCodigo())
                .nombre(asignatura.getNombre())
                .descripcion(asignatura.getDescripcion())
                .creditos(asignatura.getCreditos())
                .profesorId(asignatura.getProfesorId())
                .nivel(asignatura.getNivel())
                .activa(asignatura.isActiva())
                .build();
    }
}