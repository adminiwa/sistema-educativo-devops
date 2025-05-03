package com.example.asignaturas.controller;

import com.example.asignaturas.dto.AsignaturaRequest;
import com.example.asignaturas.dto.AsignaturaResponse;
import com.example.asignaturas.service.AsignaturaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaturas")
@RequiredArgsConstructor
public class AsignaturaController {
    
    private final AsignaturaService asignaturaService;
    
    @PostMapping
    public ResponseEntity<AsignaturaResponse> crear(@Valid @RequestBody AsignaturaRequest asignaturaRequest) {
        return new ResponseEntity<>(asignaturaService.crearAsignatura(asignaturaRequest), HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<AsignaturaResponse>> obtenerTodas() {
        return ResponseEntity.ok(asignaturaService.obtenerTodas());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AsignaturaResponse> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(asignaturaService.obtenerPorId(id));
    }
    
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<AsignaturaResponse> obtenerPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(asignaturaService.obtenerPorCodigo(codigo));
    }
    
    @GetMapping("/profesor/{profesorId}")
    public ResponseEntity<List<AsignaturaResponse>> obtenerPorProfesor(@PathVariable String profesorId) {
        return ResponseEntity.ok(asignaturaService.obtenerPorProfesor(profesorId));
    }
    
    @GetMapping("/nivel/{nivel}")
    public ResponseEntity<List<AsignaturaResponse>> obtenerPorNivel(@PathVariable String nivel) {
        return ResponseEntity.ok(asignaturaService.obtenerPorNivel(nivel));
    }
    
    @GetMapping("/activas")
    public ResponseEntity<List<AsignaturaResponse>> obtenerActivas() {
        return ResponseEntity.ok(asignaturaService.obtenerAsignaturasActivas());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AsignaturaResponse> actualizar(@PathVariable String id, 
                                                       @Valid @RequestBody AsignaturaRequest asignaturaRequest) {
        return ResponseEntity.ok(asignaturaService.actualizarAsignatura(id, asignaturaRequest));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        asignaturaService.eliminarAsignatura(id);
        return ResponseEntity.noContent().build();
    }
}