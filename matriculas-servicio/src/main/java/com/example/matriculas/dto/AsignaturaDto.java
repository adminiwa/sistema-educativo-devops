package com.example.matriculas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaDto {
    private String id;
    private String codigo;
    private String nombre;
    private int creditos;
    private String profesorId;
}