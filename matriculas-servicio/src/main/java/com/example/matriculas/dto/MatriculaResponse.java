package com.example.matriculas.dto;

import com.example.matriculas.model.Matricula;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaResponse {
    private String id;
    private String estudianteId;
    private String asignaturaId;
    private LocalDateTime fechaMatricula;
    private Matricula.EstadoMatricula estado;
    private Double calificacion;
    private String periodoAcademico;
    
    // Información adicional obtenida de otros servicios
    private UsuarioDto estudiante;
    private AsignaturaDto asignatura;
}