package com.example.matriculas.controller;

import com.example.matriculas.dto.MatriculaRequest;
import com.example.matriculas.dto.MatriculaResponse;
import com.example.matriculas.model.Matricula;
import com.example.matriculas.service.MatriculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
@RequiredArgsConstructor
public class MatriculaController {
    
    private final MatriculaService matriculaService;
    
    @PostMapping
    public ResponseEntity<MatriculaResponse> crear(@Valid @RequestBody MatriculaRequest matriculaRequest) {
        return new ResponseEntity<>(matriculaService.crearMatricula(matriculaRequest), HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<MatriculaResponse>> obtenerTodas() {
        return ResponseEntity.ok(matriculaService.obtenerTodas());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MatriculaResponse> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(matriculaService.obtenerPorId(id));
    }
    
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<MatriculaResponse>> obtenerPorEstudiante(@PathVariable String estudianteId) {
        return ResponseEntity.ok(matriculaService.obtenerPorEstudiante(estudianteId));
    }
    
    @GetMapping("/asignatura/{asignaturaId}")
    public ResponseEntity<List<MatriculaResponse>> obtenerPorAsignatura(@PathVariable String asignaturaId) {
        return ResponseEntity.ok(matriculaService.obtenerPorAsignatura(asignaturaId));
    }
    
    @GetMapping("/periodo/{periodoAcademico}")
    public ResponseEntity<List<MatriculaResponse>> obtenerPorPeriodo(@PathVariable String periodoAcademico) {
        return ResponseEntity.ok(matriculaService.obtenerPorPeriodo(periodoAcademico));
    }
    
    @PatchMapping("/{id}/estado")
    public ResponseEntity<MatriculaResponse> actualizarEstado(@PathVariable String id, @RequestParam Matricula.EstadoMatricula estado) {
        return ResponseEntity.ok(matriculaService.actualizarEstado(id, estado));
    }
    
    @PatchMapping("/{id}/calificacion")
    public ResponseEntity<MatriculaResponse> actualizarCalificacion(@PathVariable String id, @RequestParam Double calificacion) {
        return ResponseEntity.ok(matriculaService.actualizarCalificacion(id, calificacion));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        matriculaService.eliminarMatricula(id);
        return ResponseEntity.noContent().build();
    }
}