package com.example.usuarios.controller;

import com.example.usuarios.dto.AuthRequest;
import com.example.usuarios.dto.AuthResponse;
import com.example.usuarios.dto.UsuarioRequest;
import com.example.usuarios.dto.UsuarioResponse;
import com.example.usuarios.service.AuthService;
import com.example.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    private final UsuarioService usuarioService;
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
    
    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponse> registro(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        return new ResponseEntity<>(usuarioService.crearUsuario(usuarioRequest), HttpStatus.CREATED);
    }
}