package com.example.usuarios.dto;

import com.example.usuarios.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
    
    @NotEmpty(message = "Debe especificar al menos un rol")
    private List<String> roles;
    
    @NotBlank(message = "El tipo de usuario es obligatorio")
    private String tipo;
}