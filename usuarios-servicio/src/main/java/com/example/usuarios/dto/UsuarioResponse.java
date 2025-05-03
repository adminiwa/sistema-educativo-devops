package com.example.usuarios.dto;

import com.example.usuarios.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private List<String> roles;
    private Usuario.TipoUsuario tipo;
}