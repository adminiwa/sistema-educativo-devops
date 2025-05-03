package com.example.asignaturas.dto;

import com.example.asignaturas.model.Asignatura;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaResponse {
    private String id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private int creditos;
    private String profesorId;
    private Asignatura.Nivel nivel;
    private boolean activa;
}