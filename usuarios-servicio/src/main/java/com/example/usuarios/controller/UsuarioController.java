package com.example.usuarios.controller;

import com.example.usuarios.dto.UsuarioRequest;
import com.example.usuarios.dto.UsuarioResponse;
import com.example.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    public ResponseEntity<List<UsuarioResponse>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }
    
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROFESOR')")
    public ResponseEntity<UsuarioResponse> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        return new ResponseEntity<>(usuarioService.crearUsuario(usuarioRequest), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> actualizar(@PathVariable String id, 
                                                      @Valid @RequestBody UsuarioRequest usuarioRequest) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, usuarioRequest));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}