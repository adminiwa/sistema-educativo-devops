package com.example.asignaturas.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "asignaturas")
public class Asignatura {
    
    @Id
    private String id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private int creditos;
    private String profesorId;
    private Nivel nivel;
    private boolean activa;
    
    public enum Nivel {
        BASICO,
        INTERMEDIO,
        AVANZADO
    }
}