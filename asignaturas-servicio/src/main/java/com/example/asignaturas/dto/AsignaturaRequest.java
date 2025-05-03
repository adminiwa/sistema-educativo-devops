package com.example.asignaturas.dto;

import com.example.asignaturas.model.Asignatura;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaRequest {
    
    @NotBlank(message = "El código es obligatorio")
    private String codigo;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    private String descripcion;
    
    @NotNull(message = "Los créditos son obligatorios")
    @Min(value = 1, message = "Los créditos deben ser al menos 1")
    private Integer creditos;
    
    private String profesorId;
    
    @NotBlank(message = "El nivel es obligatorio")
    private String nivel;
    
    private boolean activa = true;
}