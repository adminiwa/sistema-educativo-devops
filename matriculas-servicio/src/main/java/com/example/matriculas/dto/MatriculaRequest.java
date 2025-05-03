package com.example.matriculas.dto;

import com.example.matriculas.model.Matricula;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaRequest {
    
    @NotBlank(message = "El ID del estudiante es obligatorio")
    private String estudianteId;
    
    @NotBlank(message = "El ID de la asignatura es obligatorio")
    private String asignaturaId;
    
    @NotBlank(message = "El periodo académico es obligatorio")
    private String periodoAcademico;
}