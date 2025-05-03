package com.example.matriculas.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "matriculas")
public class Matricula {
    
    @Id
    private String id;
    private String estudianteId;
    private String asignaturaId;
    private LocalDateTime fechaMatricula;
    private EstadoMatricula estado;
    private Double calificacion;
    private String periodoAcademico;
    
    public enum EstadoMatricula {
        INSCRITA,
        ACTIVA,
        COMPLETADA,
        CANCELADA
    }
}